package clientTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLAuthDAO;
import dataAccess.MySQLDAOs.MySQLGameDAO;
import dataAccess.MySQLDAOs.MySQLUserDAO;
import model.AuthData;
import model.GameData;
import model.response.GamesResponse;
import service.ClearService;

import org.junit.jupiter.api.*;
import server.Server;
import ui.exception.ResponseException;
import ui.server.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;
  static ClearService clearService = null;

  @BeforeAll
  public static void init() throws DataAccessException {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    facade = new ServerFacade("http://localhost:" + port);

    MySQLAuthDAO mySQLAuthDAO = new MySQLAuthDAO();
    MySQLGameDAO mySQLGameDAO = new MySQLGameDAO();
    MySQLUserDAO mySQLUserDAO = new MySQLUserDAO();
    clearService = new ClearService(mySQLAuthDAO, mySQLGameDAO, mySQLUserDAO);
  }

  @BeforeEach
  void setUp() throws DataAccessException {
    clearService.clearDatabase();
  }

  @AfterAll
  static void stopServer() throws DataAccessException {
    clearService.clearDatabase();
    server.stop();
  }

  @Test
  @DisplayName("Register Success")
  void registerSuccess() throws ResponseException {
    AuthData authData = facade.register("player1", "password", "p1@email.com");
    assertTrue(authData.authToken().length() > 10);
  }

  @Test
  @DisplayName("Register Failure")
  void registerFail() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    assertThrows(ResponseException.class, () -> facade.register("player1", "password", "p1@email.com"));
  }

  @Test
  @DisplayName("Login Success")
  void loginSuccess() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    assertTrue(authData.authToken().length() > 10);
  }

  @Test
  @DisplayName("Login Failure")
  void loginFailure() throws ResponseException {
    assertThrows(ResponseException.class, () -> facade.login("player1", "password"));
  }

  @Test
  @DisplayName("Logout Success")
  void logoutSuccess() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    assertDoesNotThrow(() -> facade.logout(authData.authToken()));
  }

  @Test
  @DisplayName("Logout Failure")
  void logoutFailure() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.logout(authData.authToken());
    assertThrows(ResponseException.class, () -> facade.logout(authData.authToken()));
  }

  @Test
  @DisplayName("Create Game Success")
  void createGameSuccess() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    GameData game = facade.createGame(authData.authToken(), "the bestest game");
    assertEquals(game.gameName(), "the bestest game");
  }

  @Test
  @DisplayName("Create Game Failure")
  void createGameFailure() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.logout(authData.authToken());
    assertThrows(ResponseException.class, () -> facade.createGame(authData.authToken(), "the bestest game"));
  }

  @Test
  @DisplayName("List Games")
  void listGames() throws ResponseException {
    ArrayList<GameData> expected = new ArrayList<>();
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    expected.add(facade.createGame(authData.authToken(), "the bestest game"));
    expected.add(facade.createGame(authData.authToken(), "the game to beat all games"));
    expected.add(facade.createGame(authData.authToken(), "the greatest game of all time"));
    GamesResponse actual = facade.listGames(authData.authToken());
    assertEquals(expected.size(), actual.games().size());
  }

  @Test
  @DisplayName("List Games Failure")
  void listGamesFailure() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.createGame(authData.authToken(), "the game to beat all games");
    facade.createGame(authData.authToken(), "the greatest game of all time");
    facade.logout(authData.authToken());
    assertThrows(ResponseException.class, () -> facade.listGames(authData.authToken()));
  }

  @Test
  @DisplayName("Join Game White Team")
  void joinGameWhiteTeam() throws ResponseException {
    ArrayList<String> expected = new ArrayList<>();
    expected.add("1");
    expected.add("player1");
    expected.add(null);
    expected.add("the bestest game");

    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.joinGame(authData.authToken(), "1", "WHITE");
    GamesResponse gamesResponse = facade.listGames(authData.authToken());

    Collection<GameData> games = gamesResponse.games();
    ArrayList<String> actual = new ArrayList<>();
    for (GameData game : games) {
      actual.add(Integer.toString(game.gameID()));
      actual.add(game.whiteUsername());
      actual.add(game.blackUsername());
      actual.add(game.gameName());
    }

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Join Game Black Team")
  void joinGameBlackTeam() throws ResponseException {
    ArrayList<String> expected = new ArrayList<>();
    expected.add("1");
    expected.add(null);
    expected.add("player1");
    expected.add("the bestest game");

    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.joinGame(authData.authToken(), "1", "BLACK");
    GamesResponse gamesResponse = facade.listGames(authData.authToken());

    Collection<GameData> games = gamesResponse.games();
    ArrayList<String> actual = new ArrayList<>();
    for (GameData game : games) {
      actual.add(Integer.toString(game.gameID()));
      actual.add(game.whiteUsername());
      actual.add(game.blackUsername());
      actual.add(game.gameName());
    }

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Join Game Both Teams")
  void joinBothTeams() throws ResponseException {
    ArrayList<String> expected = new ArrayList<>();
    expected.add("1");
    expected.add("player1");
    expected.add("player1");
    expected.add("the bestest game");

    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.joinGame(authData.authToken(), "1", "WHITE");
    facade.joinGame(authData.authToken(), "1", "BLACK");
    GamesResponse gamesResponse = facade.listGames(authData.authToken());

    Collection<GameData> games = gamesResponse.games();
    ArrayList<String> actual = new ArrayList<>();
    for (GameData game : games) {
      actual.add(Integer.toString(game.gameID()));
      actual.add(game.whiteUsername());
      actual.add(game.blackUsername());
      actual.add(game.gameName());
    }

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Join Game Two Users")
  void joinGameTwoUsers() throws ResponseException {
    ArrayList<String> expected = new ArrayList<>();
    expected.add("1");
    expected.add("player1");
    expected.add("player2");
    expected.add("the bestest game");

    facade.register("player1", "password", "p1@email.com");
    AuthData authData1 = facade.login("player1", "password");
    facade.createGame(authData1.authToken(), "the bestest game");
    facade.joinGame(authData1.authToken(), "1", "WHITE");
    facade.logout(authData1.authToken());

    facade.register("player2", "password2", "p2@email.com");
    AuthData authData2 = facade.login("player2", "password2");
    facade.joinGame(authData2.authToken(), "1", "BLACK");

    GamesResponse gamesResponse = facade.listGames(authData2.authToken());

    Collection<GameData> games = gamesResponse.games();
    ArrayList<String> actual = new ArrayList<>();
    for (GameData game : games) {
      actual.add(Integer.toString(game.gameID()));
      actual.add(game.whiteUsername());
      actual.add(game.blackUsername());
      actual.add(game.gameName());
    }

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Join Game As Observer")
  void joinGameAsObserver() throws ResponseException {
    ArrayList<String> expected = new ArrayList<>();
    expected.add("1");
    expected.add(null);
    expected.add(null);
    expected.add("the bestest game");

    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.joinGame(authData.authToken(), "1", null);
    GamesResponse gamesResponse = facade.listGames(authData.authToken());

    Collection<GameData> games = gamesResponse.games();
    ArrayList<String> actual = new ArrayList<>();
    for (GameData game : games) {
      actual.add(Integer.toString(game.gameID()));
      actual.add(game.whiteUsername());
      actual.add(game.blackUsername());
      actual.add(game.gameName());
    }

    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Join Game Failure")
  void joinGameFailure() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.logout(authData.authToken());
    assertThrows(ResponseException.class, () -> facade.joinGame(authData.authToken(), "1", "BLACK"));
  }
}