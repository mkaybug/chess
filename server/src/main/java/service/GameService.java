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

    ChessGame newGame = new ChessGame();
    newGame.getBoard().resetBoard();
    return addGame(new GameData(idNum, null, null, request.gameName(), newGame));
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
    GameData game = verifyJoin(gameID, authToken);

    AuthData auth = getAuth(authToken);
    try {
      confirmGameCommand(auth.username(), playerColor, game);
    }
    catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }

    return game;
  }

  private GameData verifyJoin(int gameID, String authToken) throws DataAccessException {
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
    return game;
  }

  public GameData joinObserver(int gameID, String authToken) throws DataAccessException {
    return verifyJoin(gameID, authToken);
  }

  public GameData makeMove(int gameID, String authToken, ChessMove move) throws DataAccessException, InvalidMoveException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData gameData = getGame(gameID);
    AuthData authData = getAuth(authToken);

    if (gameData.game().isGameOver()) {
      throw new InvalidMoveException("Error: game over");
    }

    if (gameData.game().isInCheckmate(gameData.game().getTeamTurn())) {
      throw new InvalidMoveException("Error: checkmate, the game is over");
    }

    if ((gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE && !Objects.equals(gameData.whiteUsername(), authData.username())) ||
        (gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK && !Objects.equals(gameData.blackUsername(), authData.username()))){
      throw new InvalidMoveException("Error: not your turn");
    }

    try {
      gameData.game().makeMove(move);
      putGame(gameData);
      return getGame(gameID);
    }
    catch (InvalidMoveException e) {
      throw new InvalidMoveException("Error: invalid move");
    }
  }
  public void leaveGame(int gameID, String authToken) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData gameData = getGame(gameID);
    AuthData authData = getAuth(authToken);

    if (Objects.equals(authData.username(), gameData.whiteUsername())) {
      putGame(new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game()));
    }
    else if (Objects.equals(authData.username(), gameData.blackUsername())) {
      putGame(new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game()));
    }
  }
  public void resignGame(int gameID, String authToken) throws DataAccessException {
    try {
      authenticateUser(authToken);
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }

    GameData gameData = getGame(gameID);
    AuthData authData = getAuth(authToken);

    if (gameData.game().isGameOver()) {
      throw new DataAccessException("Error: game is already over, can't resign");
    }

    if (!Objects.equals(authData.username(), gameData.whiteUsername()) && !Objects.equals(authData.username(), gameData.blackUsername())) {
      throw new DataAccessException("Error: observers can't resign, leave instead");
    }

    gameData.game().setGameOver(true);
    putGame(gameData);
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
