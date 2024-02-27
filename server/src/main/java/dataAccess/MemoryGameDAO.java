package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private final HashMap<Integer, GameData> gameDataMap = new HashMap<>();

  // Insert game (gameID, whiteUsername, blackUsername, gameName, and ChessGame game)
  public GameData addGame(GameData game) {
    game = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    gameDataMap.put(game.gameID(), game);
    return game;
  }

  // Select game using gameID
  public GameData getGame(int gameID) {
    return gameDataMap.get(gameID);
  }

  // Select all games
  public Collection<GameData> listGames() {
    return gameDataMap.values();
  }

  // Put game
  public void putGame(GameData game) {
    gameDataMap.put(game.gameID(), game);
  }

  // Delete game from gameDataMap
  public void deleteGame(int gameID) {
    gameDataMap.remove(gameID);
  }

  // Clear gameDataMap
  public void deleteAllGames() {
    gameDataMap.clear();
  }
}
