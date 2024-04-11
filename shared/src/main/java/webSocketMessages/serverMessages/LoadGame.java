package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
  GameData game;
  public LoadGame(GameData game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public GameData getGame() {
    return game;
  }

  public void setGame(GameData game) {
    this.game = game;
  }
}
