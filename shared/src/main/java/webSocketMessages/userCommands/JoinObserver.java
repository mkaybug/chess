package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
  private int gameID;
  public JoinObserver(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }

  public void setGameID(int gameID) {
    this.gameID = gameID;
  }

}