package server.websocket;

import chess.ChessGame;
import chess.ChessMove;

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
            case MAKE_MOVE -> makeMove(session, message);
//            case LEAVE:
//                Leave leaveCommand = new Gson().fromJson(message, Leave.class);
//                leave(leaveCommand.getGameID());
//            case RESIGN:
//                Resign resignCommand = new Gson().fromJson(message, Resign.class);
//                leave(resignCommand.getGameID());
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

            String returnMessage = "";
            if (playerColor == ChessGame.TeamColor.WHITE) {
                returnMessage = String.format("%s joined game %s on team %s.", game.whiteUsername(), gameID, playerColor);
            }
            if (playerColor == ChessGame.TeamColor.BLACK) {
                returnMessage = String.format("%s joined game %s on team %s.", game.blackUsername(), gameID, playerColor);
            }
            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);

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

        try {
            GameData game = gameService.joinObserver(gameID, authToken);
            AuthData auth = gameService.getAuth(authToken);

            connections.add(gameID, authToken, session);

            String broadcastMessage = String.format("%s joined game %s as an observer.", auth.username(), gameID);

            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);

            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);
        }
        catch (DataAccessException e){
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

    private void makeMove(Session session, String message) throws IOException {
        MakeMove command = new Gson().fromJson(message, MakeMove.class);

        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessMove move = command.getMove();

        try {
            GameData game = gameService.makeMove(gameID, authToken, move);
            AuthData auth = gameService.getAuth(authToken);

            String broadcastMessage = String.format("%s made a move.", auth.username(), gameID);

            Notification notification = new Notification(broadcastMessage);
            connections.broadcast(gameID, authToken, notification);

            LoadGame loadGame = new LoadGame(game);
            connections.sendIndividualMessage(authToken, loadGame);
        }
        catch (DataAccessException e){
            Error error = new Error(e.getMessage());
            connections.sendIndividualMessage(authToken, error);
        }
    }

//    private void makeMove(int gameID, ChessMove move) throws IOException {
//        connections.add(gameID, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void leave(int gameID) throws IOException {
//        connections.remove(gameID);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(message);
//        connections.broadcast(visitorName, notification);
//    }
}