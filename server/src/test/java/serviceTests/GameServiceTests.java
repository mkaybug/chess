package serviceTests;

import chess.ChessGame;

import dataAccess.MemoryDAOs.MemoryAuthDAO;
import dataAccess.MemoryDAOs.MemoryGameDAO;
import dataAccess.MemoryDAOs.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.*;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
  GameService gameService = null;
  ClearService clearService = null;
  UserService userService = null;

  @BeforeEach
  void setUp() {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    gameService = new GameService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    userService = new UserService(memoryAuthDAO, memoryUserDAO);
  }

  @Test
  @DisplayName("List Games Success Empty")
  void listGamesSuccessEmpty() throws DataAccessException {
    AuthData testAuth = new AuthData("token4", "white4");
    List<GameData> expected = new ArrayList<>();
    userService.addAuth(new AuthData("token1", "white1"));
    userService.addAuth(new AuthData("token2", "white1"));
    userService.addAuth(new AuthData("token3", "black2"));

    // Positive test -> listGames is empty
    userService.addAuth(testAuth);
    assertIterableEquals(new ArrayList<>(), gameService.listGames(testAuth.authToken()));
  }

  @Test
  @DisplayName("List Games Success Not Empty")
  void listGamesSuccessNotEmpty() throws DataAccessException {
    AuthData testAuth = new AuthData("token4", "white4");
    List<GameData> expected = new ArrayList<>();
    userService.addAuth(new AuthData("token1", "white1"));
    userService.addAuth(new AuthData("token2", "white1"));
    userService.addAuth(new AuthData("token3", "black2"));

    // Positive test -> listGames is not empty
    userService.addAuth(testAuth);

    expected.add(gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame())));
    expected.add(gameService.addGame(new GameData(2, "white2", "black2", "game2", new ChessGame())));
    expected.add(gameService.addGame(new GameData(3, "white3", "black3", "game3", new ChessGame())));

    var actual = gameService.listGames(testAuth.authToken());
    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("List Games Unauthorized")
  void listGamesUnauthorized() throws DataAccessException {
    AuthData testAuth = new AuthData("token4", "white4");
    userService.addAuth(new AuthData("token1", "white1"));
    userService.addAuth(new AuthData("token2", "white1"));
    userService.addAuth(new AuthData("token3", "black2"));

    // Negative test -> not authorized so listGames fails
    assertThrows(DataAccessException.class, () -> gameService.listGames(testAuth.authToken()));
  }

  @Test
  @DisplayName("Create Game Null Field")
  void createGameNull() throws DataAccessException {
    AuthData auth = new AuthData("auth", "username");
    CreateGameRequest nullRequest = new CreateGameRequest(null);

    // Negative test case -> null gameName so fails
    assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), nullRequest));
  }

  @Test
  @DisplayName("Create Game Unauthorized")
  void createGameUnauthorized() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth = new AuthData("auth", "username");
    CreateGameRequest request = new CreateGameRequest(game.gameName());

    // Negative test case -> authentication fails
    assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), request));
  }

  @Test
  @DisplayName("Create First Game")
  void createFirstGame() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth = new AuthData("auth", "username");
    UserData user = new UserData("username", "password", "email@gmail.com");
    CreateGameRequest request = new CreateGameRequest(game.gameName());

    userService.addUser(user);
    userService.addAuth(auth);

    // Positive test case (testing each field separately because new ChessGames will always have different addresses)
    GameData newGame = gameService.createGame(auth.authToken(), request);
    assertEquals(game.whiteUsername(), newGame.whiteUsername());
    assertEquals(game.blackUsername(), newGame.blackUsername());
    assertEquals(game.gameName(), newGame.gameName());
  }

  @Test
  @DisplayName("Create Second Game")
  void createSecondGame() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth = new AuthData("auth", "username");
    UserData user = new UserData("username", "password", "email@gmail.com");
    CreateGameRequest request = new CreateGameRequest(game.gameName());

    userService.addUser(user);
    userService.addAuth(auth);

    // Creating first game
    gameService.createGame(auth.authToken(), request);

    // Adding a second game
    GameData secondGame = gameService.createGame(auth.authToken(), request);
    assertEquals(game.whiteUsername(), secondGame.whiteUsername());
    assertEquals(game.blackUsername(), secondGame.blackUsername());
    assertEquals(game.gameName(), secondGame.gameName());
  }

  @Test
  @DisplayName("Join Game Unauthorized")
  void joinGameUnauthorized() throws DataAccessException {
    AuthData auth1 = new AuthData("auth1", "username1");
    JoinGameRequest requestWhite = new JoinGameRequest("WHITE", 1);

    // Negative test case -> authentication fails
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), requestWhite));
  }

  @Test
  @DisplayName("Join Game Invalid ID")
  void joinGameInvalidID() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth1 = new AuthData("auth1", "username1");
    UserData user1 = new UserData("username1", "password1", "email1@gmail.com");
    JoinGameRequest nullRequest = new JoinGameRequest("WHITE", 0);

    userService.addUser(user1);
    userService.addAuth(auth1);
    gameService.addGame(game);

    // Negative test case -> invalid gameID
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), nullRequest));
  }

  @Test
  @DisplayName("Join Game White Pass")
  void joinGameWhitePass() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth1 = new AuthData("auth1", "username1");
    UserData user1 = new UserData("username1", "password1", "email1@gmail.com");
    JoinGameRequest requestWhite = new JoinGameRequest("WHITE", 1);

    userService.addUser(user1);
    userService.addAuth(auth1);
    gameService.addGame(game);

    // Positive test case -> white passes
    gameService.joinGame(auth1.authToken(), requestWhite);
    assertEquals("username1", gameService.getGame(game.gameID()).whiteUsername());
  }

  @Test
  @DisplayName("Join Game White Taken")
  void joinGameWhiteTaken() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth1 = new AuthData("auth1", "username1");
    UserData user1 = new UserData("username1", "password1", "email1@gmail.com");
    JoinGameRequest requestWhite = new JoinGameRequest("WHITE", 1);

    userService.addUser(user1);
    userService.addAuth(auth1);
    gameService.addGame(game);

    gameService.joinGame(auth1.authToken(), requestWhite);

    // Negative test case -> white already taken
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), requestWhite));
  }

  @Test
  @DisplayName("Join Game Black Pass")
  void joinGameBlackPass() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth2 = new AuthData("auth2", "username2");
    UserData user2 = new UserData("username2", "password2", "email2@gmail.com");
    JoinGameRequest requestBlack = new JoinGameRequest("BLACK", 1);

    userService.addUser(user2);
    userService.addAuth(auth2);

    gameService.addGame(game);

    // Positive test case -> black passes
    gameService.joinGame(auth2.authToken(), requestBlack);
    assertEquals("username2", gameService.getGame(game.gameID()).blackUsername());
  }

  @Test
  @DisplayName("Join Game Black Taken")
  void joinGameBlackTaken() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth2 = new AuthData("auth2", "username2");
    UserData user2 = new UserData("username2", "password2", "email2@gmail.com");
    JoinGameRequest requestBlack = new JoinGameRequest("BLACK", 1);

    userService.addUser(user2);
    userService.addAuth(auth2);
    gameService.addGame(game);

    gameService.joinGame(auth2.authToken(), requestBlack);

    // Negative test case -> black already taken
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth2.authToken(), requestBlack));
  }

  @Test
  @DisplayName("Delete Game")
  void deleteGame() throws DataAccessException {
    List<GameData> expected = new ArrayList<>();
    var game = gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
    expected.add(gameService.addGame(new GameData(2, "white2", "black2", "game2", new ChessGame())));
    expected.add(gameService.addGame(new GameData(3, "white3", "black3", "game3", new ChessGame())));

    gameService.deleteGame(game.gameID());
    var actual = clearService.listGames();
    assertIterableEquals(expected, actual);
  }
}
