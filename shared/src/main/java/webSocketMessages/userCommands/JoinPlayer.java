package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
  private int gameID;
  private ChessGame.TeamColor playerColor;
  public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
    this.playerColor = playerColor;
  }

  public int getGameID() {
    return gameID;
  }

  public void setGameID(int gameID) {
    this.gameID = gameID;
  }


  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(ChessGame.TeamColor playerColor) {
    this.playerColor = playerColor;
  }
}
