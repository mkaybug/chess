package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayer joinPlayerCommand = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(joinPlayerCommand.getGameID(), joinPlayerCommand.getAuthString(), joinPlayerCommand.getPlayerColor(), session);
                break;
            case JOIN_OBSERVER:
                JoinObserver joinObserverCommand = new Gson().fromJson(message, JoinObserver.class);
                joinObserver(joinObserverCommand.getGameID(), joinObserverCommand.getAuthString(), session);
//            case MAKE_MOVE:
//                MakeMove makeMoveCommand = new Gson().fromJson(message, MakeMove.class);
//                makeMove(makeMoveCommand.getGameID(), makeMoveCommand.getMove());
//            case LEAVE:
//                Leave leaveCommand = new Gson().fromJson(message, Leave.class);
//                leave(leaveCommand.getGameID());
//            case RESIGN:
//                Resign resignCommand = new Gson().fromJson(message, Resign.class);
//                leave(resignCommand.getGameID());
        }
    }

    private void joinPlayer(int gameID, String authString, ChessGame.TeamColor teamColor, Session session) throws IOException {
        connections.add(gameID, authString, session);
        // FIXME later I need to figure out a way to get the username in this message
        var message = String.format("%s joined game %s on team %s", authString, gameID, teamColor);
        Notification notification = new Notification(message);
        connections.broadcast(gameID, authString, notification);
    }

    private void joinObserver(int gameID, String authString, Session session) throws IOException {
        connections.add(gameID, authString, session);
        // FIXME same, put the username in here
        var message = String.format("%s joined game %s as an observer", authString, gameID);
        Notification notification = new Notification(message);
        connections.broadcast(gameID, authString, notification);
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