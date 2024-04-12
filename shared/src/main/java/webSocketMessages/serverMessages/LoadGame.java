package webSocketMessages.serverMessages;

import model.GameData;
import com.google.gson.Gson;

public class LoadGame extends ServerMessage {
  private GameData game;
  public LoadGame(GameData game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public GameData getGame() {
    return game;
  }

  public String toString() {
    return new Gson().toJson(this);
  }
}
