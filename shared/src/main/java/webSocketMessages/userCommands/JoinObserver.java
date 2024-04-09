package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
  int gameID;
  public JoinObserver(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
  }
}