package ui.websocket.messageHandler;

import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void handleNotification(Notification notification);
    void handleError(Error error);
    void handleLoadGame(LoadGame game);
}
