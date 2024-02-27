package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
  private final GameDAO gameDataAccess;

  public GameService(GameDAO gameDataAccess) {
    this.gameDataAccess = gameDataAccess;
  }

  public GameData addGame(GameData game) throws DataAccessException {
    return gameDataAccess.addGame(game);
  }

  public GameData getGame(int gameID) throws DataAccessException {
    return gameDataAccess.getGame(gameID);
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return gameDataAccess.listGames();
  }

  public void putGame(GameData game) throws DataAccessException {
    gameDataAccess.putGame(game);
  }

  public void deleteGame(int gameID) throws DataAccessException {
    gameDataAccess.deleteGame(gameID);
  }

  public void deleteAllGames() throws DataAccessException {
    gameDataAccess.deleteAllGames();
  }
}
