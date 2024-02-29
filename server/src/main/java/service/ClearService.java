package service;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ClearService {
  private final AuthDAO authDataAccess;
  private final GameDAO gameDataAccess;
  private final UserDAO userDataAccess;

  public ClearService(AuthDAO authDataAccess, GameDAO gameDataAccess, UserDAO userDataAccess) {
    this.authDataAccess = authDataAccess;
    this.gameDataAccess = gameDataAccess;
    this.userDataAccess = userDataAccess;
  }

  public void clearDatabase() throws DataAccessException {
    deleteAllAuthTokens();
    deleteAllGames();
    deleteAllUsers();
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return authDataAccess.listAuthTokens();
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    authDataAccess.deleteAllAuthTokens();
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return gameDataAccess.listGames();
  }

  public void deleteAllGames() throws DataAccessException {
    gameDataAccess.deleteAllGames();
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return userDataAccess.listUsers();
  }

  public void deleteAllUsers() throws DataAccessException {
    userDataAccess.deleteAllUsers();
  }
}
