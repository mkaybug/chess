package dataAccess.MySQLDAOs;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class MySQLGameDAO implements GameDAO {
  public GameData addGame(GameData game) throws DataAccessException {
    return null;
  }

  public GameData getGame(int gameID) throws DataAccessException {
    return null;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return null;
  }

  public void putGame(GameData game) throws DataAccessException {

  }

  public void deleteGame(int gameID) throws DataAccessException {

  }

  public void deleteAllGames() throws DataAccessException {

  }
}
