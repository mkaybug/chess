package service;

import chess.ChessGame;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;

import java.util.Collection;
import java.util.Objects;

public class GameService {
  private final AuthDAO authDataAccess;
  private final GameDAO gameDataAccess;

  public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess, UserDAO userDataAccess) {
    this.authDataAccess = authDataAccess;
    this.gameDataAccess = gameDataAccess;
  }

  public void authenticateUser(String authToken) throws DataAccessException {
    AuthData existingAuth = getAuth(authToken);
    if (existingAuth == null) {
      throw new DataAccessException("Error: unauthorized");
    }
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken);
  }

  public Collection<GameData> listGames(String authToken) throws DataAccessException {
    try {
      authenticateUser(authToken);
      // Return list of games
      return gameDataAccess.listGames();
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }
  }

  public GameData createGame(String authToken, CreateGameRequest request) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    if (request.gameName() == null) {
      throw new DataAccessException("Error: bad request");
    }

    int idNum = 1;
    while (getGame(idNum) != null) {
      idNum += 1;
    }
    return addGame(new GameData(idNum, null, null, request.gameName(), new ChessGame()));
  }

  public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
    // Authenticate user
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    // If gameID is null or doesn't correspond to a real game
    if (request.gameID() == 0 || getGame(request.gameID()) == null) {
      throw new DataAccessException("Error: bad request");
    }

    // Get the game
    GameData game = getGame(request.gameID());
    if (Objects.equals(request.playerColor(), "WHITE")) {
      if (game.whiteUsername() != null) {
        throw new DataAccessException("Error: already taken");
      }
      putGame(new GameData (game.gameID(), getAuth(authToken).username(), null, game.gameName(), game.game()));
    }
    if (Objects.equals(request.playerColor(), "BLACK")) {
      if (game.blackUsername() != null) {
        throw new DataAccessException("Error: already taken");
      }
      putGame(new GameData (game.gameID(), null, getAuth(authToken).username(), game.gameName(), game.game()));
    }
    // Observer -> empty string
    // Make sure the game exists (since we've already done that, we do nothing
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
