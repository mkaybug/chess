package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
  int gameID;
  ChessGame.TeamColor playerColor;
  public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_OBSERVER;
    this.gameID = gameID;
    this.playerColor = playerColor;
  }
}
