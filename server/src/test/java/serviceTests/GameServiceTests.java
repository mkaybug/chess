package serviceTests;

import chess.ChessGame;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.*;

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
  void listGames() throws DataAccessException {
    AuthData testAuth = new AuthData("token4", "white4");
    List<GameData> expected = new ArrayList<>();
    userService.addAuth(new AuthData("token1", "white1"));
    userService.addAuth(new AuthData("token2", "white1"));
    userService.addAuth(new AuthData("token3", "black2"));

    // Negative test -> not authorized so listGames fails
    assertThrows(DataAccessException.class, () -> gameService.listGames(testAuth.authToken()));

    // Positive test -> listGames is empty
    userService.addAuth(testAuth);
    assertIterableEquals(new ArrayList<>(), gameService.listGames(testAuth.authToken()));

    // Positive test -> listGames is not empty
    expected.add(gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame())));
    expected.add(gameService.addGame(new GameData(2, "white2", "black2", "game2", new ChessGame())));
    expected.add(gameService.addGame(new GameData(3, "white3", "black3", "game3", new ChessGame())));

    var actual = gameService.listGames(testAuth.authToken());
    assertIterableEquals(expected, actual);
  }

  @Test
  void createGame() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());
    AuthData auth = new AuthData("auth", "username");
    UserData user = new UserData("username", "password", "email@gmail.com");
    CreateGameRequest request = new CreateGameRequest(game.gameName());
    CreateGameRequest nullRequest = new CreateGameRequest(null);

    // Negative test case -> authentication fails
    assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), request));

    userService.addUser(user);
    userService.addAuth(auth);

    // Positive test case (testing each field separately because new ChessGames will always have different addresses
    GameData newGame = gameService.createGame(auth.authToken(), request);
    assertEquals(game.whiteUsername(), newGame.whiteUsername());
    assertEquals(game.blackUsername(), newGame.blackUsername());
    assertEquals(game.gameName(), newGame.gameName());

    // Adding a second game
    GameData secondGame = gameService.createGame(auth.authToken(), request);
    assertEquals(game.whiteUsername(), newGame.whiteUsername());
    assertEquals(game.blackUsername(), newGame.blackUsername());
    assertEquals(game.gameName(), newGame.gameName());

    // Negative test case -> null gameName so fails
    assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), nullRequest));
  }

  @Test
  void joinGame() throws DataAccessException {
    GameData game = new GameData(1, null, null, "game1", new ChessGame());

    AuthData auth1 = new AuthData("auth1", "username1");
    AuthData auth2 = new AuthData("auth2", "username2");

    UserData user1 = new UserData("username1", "password1", "email1@gmail.com");
    UserData user2 = new UserData("username2", "password2", "email2@gmail.com");

    JoinGameRequest requestWhite = new JoinGameRequest("WHITE", 1);
    JoinGameRequest requestBlack = new JoinGameRequest("BLACK", 1);
    JoinGameRequest nullRequest = new JoinGameRequest("WHITE", 0);

    // Negative test case -> authentication fails
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), requestWhite));

    userService.addUser(user1);
    userService.addUser(user2);
    userService.addAuth(auth1);
    userService.addAuth(auth2);

    // Negative test case -> game doesn't exist
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), requestWhite));

    gameService.addGame(game);

    // Negative test case -> invalid gameID
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), nullRequest));

    // Positive test case -> white passes
    gameService.joinGame(auth1.authToken(), requestWhite);

    assertEquals("username1", gameService.getGame(game.gameID()).whiteUsername());

    // Negative test case -> white already taken
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth1.authToken(), requestWhite));

    // Positive test case -> black passes
    gameService.joinGame(auth2.authToken(), requestBlack);
    assertEquals("username2", gameService.getGame(game.gameID()).blackUsername());

    // Negative test case -> black already taken
    assertThrows(DataAccessException.class, () -> gameService.joinGame(auth2.authToken(), requestBlack));
  }

  @Test
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
