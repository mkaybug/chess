package ui.websocket;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

public interface NotificationHandler {
    void handleServerMessage(ServerMessage serverMessage);
    void handleClientMessage(UserGameCommand userMessage);
}
