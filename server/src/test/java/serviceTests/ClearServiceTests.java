package serviceTests;

import chess.ChessGame;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.AuthData;
import model.GameData;
import model.UserData;

import service.ClearService;
import service.GameService;
import service.UserService;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {
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
  void clearDatabase() throws DataAccessException {
    gameService.addGame(new GameData(1, "white1", "black1", "game1", new ChessGame()));
    gameService.addGame(new GameData(2, "white2", "black2", "game2", new ChessGame()));
    gameService.addGame(new GameData(3, "white3", "black3", "game3", new ChessGame()));

    userService.addUser(new UserData("user1", "password1", "email1@gmail.com"));
    userService.addUser(new UserData("user2", "password2", "email2@gmail.com"));
    userService.addUser(new UserData("user3", "password3", "email3@gmail.com"));

    userService.addAuth(new AuthData("token1", "username1"));
    userService.addAuth(new AuthData("token2", "username2"));
    userService.addAuth(new AuthData("token3", "username3"));

    clearService.clearDatabase();
    assertEquals(0, clearService.listGames().size());
    assertEquals(0, clearService.listUsers().size());
    assertEquals(0, clearService.listAuthTokens().size());

  }

}
