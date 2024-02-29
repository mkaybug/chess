package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {
  private final AuthDAO authDataAccess;
  private final GameDAO gameDataAccess;
  private final UserDAO userDataAccess;

  public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess, UserDAO userDataAccess) {
    this.authDataAccess = authDataAccess;
    this.gameDataAccess = gameDataAccess;
    this.userDataAccess = userDataAccess;
  }

  public AuthData authenticateUser(AuthData auth) throws DataAccessException {
    // Check if auth already exists
    AuthData existingAuth = getAuth(auth.authToken());
    if (existingAuth == null) {
      throw new DataAccessException("No authentication.");
    }
    else {
      return existingAuth;
    }
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken);
  }

  public Collection<GameData> listGames(AuthData auth) throws DataAccessException {
    // Authenticate user
    try {
      authenticateUser(auth);
      // Return list of games
      try {
        return gameDataAccess.listGames();
      }
      catch (DataAccessException e){
        throw new DataAccessException("There are no games to list.");
      }
    }
    // If auth doesn't exist, throw error
    catch (DataAccessException e) {
      throw new DataAccessException("Please login to see games.");
    }
  }

  public GameData addGame(GameData game) throws DataAccessException {
    return gameDataAccess.addGame(game);
  }

  public GameData getGame(int gameID) throws DataAccessException {
    return gameDataAccess.getGame(gameID);
  }

  public void putGame(GameData game) throws DataAccessException {
    gameDataAccess.putGame(game);
  }

  public void deleteGame(int gameID) throws DataAccessException {
    gameDataAccess.deleteGame(gameID);
  }
}
