package service;

import chess.ChessGame;

import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.request.CreateGameRequest;
import model.GameData;
import model.request.JoinGameRequest;

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
    // FIXME Change to only return idNum
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
      putGame(new GameData (game.gameID(), getAuth(authToken).username(), game.blackUsername(), game.gameName(), game.game()));
    }
    if (Objects.equals(request.playerColor(), "BLACK")) {
      if (game.blackUsername() != null) {
        throw new DataAccessException("Error: already taken");
      }
      putGame(new GameData (game.gameID(), game.whiteUsername(), getAuth(authToken).username(), game.gameName(), game.game()));
    }
    // Observer -> empty string
    // Make sure the game exists (since we've already done that, we do nothing
  }

  private void confirmGameCommand(String username, ChessGame.TeamColor playerColor, GameData game) throws DataAccessException {
    if (playerColor == ChessGame.TeamColor.WHITE) {
      if (!Objects.equals(game.whiteUsername(), username)) {
        throw new DataAccessException("Error: team WHITE is being played by someone else");
      }
    }
    if (playerColor == ChessGame.TeamColor.BLACK) {
      if (!Objects.equals(game.blackUsername(), username)) {
        throw new DataAccessException("Error: team BLACK is being played by someone else");
      }
    }
  }

  public GameData joinPlayer(int gameID, String authToken, ChessGame.TeamColor playerColor) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData game;
    try {
      game = getGame(gameID);
      if (game == null) {
        throw new NullPointerException(); // Or any other appropriate exception
      }
    }
    catch (Exception e) {
      throw new DataAccessException("Error: invalid game ID");
    }

    AuthData auth = getAuth(authToken);
    try {
      confirmGameCommand(auth.username(), playerColor, game);
    }
    catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }

    return game;
  }

  public GameData joinObserver(int gameID, String authToken) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    try {
      GameData game = getGame(gameID);
      if (game == null) {
        throw new NullPointerException(); // Or any other appropriate exception
      }
      return game;
    }
    catch (Exception e) {
      throw new DataAccessException("Error: invalid game ID");
    }
  }

  // FIXME Where I left off: Wrote this out, haven't tested it yet, am I handling all the exceptions? I don't know.
  public GameData makeMove(int gameID, String authToken, ChessMove move) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData gameData = getGame(gameID);

    try {
      gameData.game().makeMove(move);
      putGame(gameData);
      return getGame(gameID);
    }
    catch (InvalidMoveException e) {
      throw new DataAccessException("Error: invalid move");
    }
  }
  public void leaveGame() {}
  public void resignGame() {}

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
