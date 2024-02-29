package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.GameService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
  GameService gameService = null;
  ClearService clearService = null;


  @BeforeEach
  void setUp() {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    gameService = new GameService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
    clearService = new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
  }

//  @Test
//  void addUser() throws DataAccessException {
//    var user = new UserData("mblack", "school_password", "mblack15@byu.edu");
//    user = userService.addUser(user);
//
//    var users = userService.listUsers();
//    assertEquals(1, users.size());
//    assertTrue(users.contains(user));
//  }

  @Test
  void listGames() throws DataAccessException {
    AuthData testAuth = new AuthData("token4", "white4");
    List<GameData> expected = new ArrayList<>();
    List<AuthData> authTokens = new ArrayList<>();
//    authService.addAuth(new AuthData("token1", "white1"));
//    authService.addAuth(new AuthData("token2", "white1"));
//    authService.addAuth(new AuthData("token3", "black2"));

    // Negative test -> not authorized so listGames fails
    assertThrows(DataAccessException.class, () -> gameService.listGames(testAuth));

    // Positive test -> listGames is empty
//    authService.addAuth(testAuth);
    assertNull(gameService.listGames(testAuth));

    // Positive test -> listGames is not empty
    expected.add(gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame())));
    expected.add(gameService.addGame(new GameData(1, "white2", "black2", "game2", new ChessGame())));
    expected.add(gameService.addGame(new GameData(1, "white3", "black3", "game3", new ChessGame())));

    var actual = gameService.listGames(testAuth);
    assertIterableEquals(expected, actual);
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

  @Test
  void deleteAllGames() throws DataAccessException {
    gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
    gameService.addGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
    gameService.addGame(new GameData(3, "white3", "black3", "game3", new ChessGame()));

    clearService.deleteAllGames();
    assertEquals(0, clearService.listGames().size());
  }

}
