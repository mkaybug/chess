package server.websocket;

import chess.ChessGame;
import chess.ChessMove;

import chess.ChessPiece;
import com.google.gson.Gson;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import service.GameService;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leave(message);
            case RESIGN -> resign(message);
        }
    }

    private void joinPlayer(Session session, String message) throws IOException {
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessGame.TeamColor playerColor = command.getPlayerColor();

        connections.add(gameID, authToken, session);
        try {
            GameData game = gameService.joinPlayer(gameID, authToken, playerColor);

            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);

            String returnMessage = "";
            if (playerColor == ChessGame.TeamColor.WHITE) {
                returnMessage = String.format("%s joined game %s on team %s.", game.whiteUsername(), gameID, playerColor);
            }
            if (playerColor == ChessGame.TeamColor.BLACK) {
                returnMessage = String.format("%s joined game %s on team %s.", game.blackUsername(), gameID, playerColor);
            }
            Notification notification = new Notification(returnMessage);
            connections.broadcast(gameID, authToken, notification);
        }
        catch (DataAccessException e){
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

    private void joinObserver(Session session, String message) throws IOException {
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();

        connections.add(gameID, authToken, session);

        try {
            GameData game = gameService.joinObserver(gameID, authToken);
            AuthData auth = gameService.getAuth(authToken);

            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);

            String broadcastMessage = String.format("%s joined game %s as an observer.", auth.username(), gameID);
            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);
        }
        catch (DataAccessException e){
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

    private void makeMove(String message) throws IOException {
        MakeMove command = new Gson().fromJson(message, MakeMove.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessMove move = command.getMove();

        try {
            GameData game = gameService.makeMove(gameID, authToken, move);
            AuthData auth = gameService.getAuth(authToken);

            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);
            connections.broadcast(gameID, authToken, loadGame);

            String pieceMoved = game.game().getBoard().getPiece(move.getEndPosition()).getPieceType().name();
            String startPosition = move.getStartPosition().toString();
            String endPosition = move.getEndPosition().toString();
            String broadcastMessage = String.format("%s moved %s from %s to %s.", auth.username(), pieceMoved, startPosition, endPosition);
            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);
        }
        catch (Exception e){
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

    public void leave(String message) throws IOException {
        Leave command = new Gson().fromJson(message, Leave.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();

        try {
            AuthData authData = gameService.getAuth(command.getAuthString());
            gameService.leaveGame(gameID, authToken);
            connections.remove(command.getAuthString());

            String broadcastMessage = String.format("%s has left the game.", authData.username());
            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);
        }
        catch (Exception e) {
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

    public void resign(String message) throws IOException {
        Resign command = new Gson().fromJson(message, Resign.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();

        try {
            AuthData authData = gameService.getAuth(command.getAuthString());
            gameService.resignGame(gameID, authToken);

            String broadcastMessage = String.format("%s has resigned.", authData.username());
            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);
            connections.sendIndividualMessage(authToken, notification);

            connections.remove(command.getAuthString());
        }
        catch (Exception e) {
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }
}