package clientTests;

import dataAccess.AuthDAO;
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
import ui.Repl;
import ui.exception.ResponseException;
import ui.server.ServerFacade;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;
  ClearService clearService = null;

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(8080);
    System.out.println("Started test HTTP server on " + port);
    facade = new ServerFacade("http://localhost:8080");
  }

  @BeforeEach
  void setUp() throws DataAccessException {
    MySQLAuthDAO mySQLAuthDAO = new MySQLAuthDAO();
    MySQLGameDAO mySQLGameDAO = new MySQLGameDAO();
    MySQLUserDAO mySQLUserDAO = new MySQLUserDAO();

    clearService = new ClearService(mySQLAuthDAO, mySQLGameDAO, mySQLUserDAO);
  }

  @AfterEach
  void clearDatabase() throws DataAccessException {
    clearService.clearDatabase();
  }

  @AfterAll
  static void stopServer() {
    server.stop();
  }

  @Test
  @DisplayName("Register")
  void register() throws ResponseException {
    AuthData authData = facade.register("player1", "password", "p1@email.com");
    assertTrue(authData.authToken().length() > 10);
  }

  @Test
  @DisplayName("Login")
  void login() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    assertTrue(authData.authToken().length() > 10);
  }

  @Test
  @DisplayName("Logout")
  void logout() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    assertDoesNotThrow(() -> facade.logout(authData.authToken()));
  }

  @Test
  @DisplayName("Create Game")
  void createGame() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    GameData game = facade.createGame(authData.authToken(), "the bestest game");
    assertEquals(game.gameName(), "the bestest game");
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
  @DisplayName("Join Game White Team")
  void joinGameWhiteTeam() throws ResponseException {
    facade.register("player1", "password", "p1@email.com");
    AuthData authData = facade.login("player1", "password");
    facade.createGame(authData.authToken(), "the bestest game");
    facade.joinGame(authData.authToken(), "WHITE", "1");
  }

  @Test
  void testJoinGameCommand() {
    try {
      // Connect to localhost
      Socket socket = new Socket("localhost", 8080);
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Send register command
      String registerCommand = "register username password email@gmail.com\n";
      outputStream.write(registerCommand.getBytes());
      outputStream.flush();

      // Send login command
      String loginCommand = "login username password\n";
      outputStream.write(loginCommand.getBytes());
      outputStream.flush();

      // Send createGame command
      String createFirstGameCommand = "createGame the greatest game\n";
      outputStream.write(createFirstGameCommand.getBytes());
      outputStream.flush();

      // Send joinGame command
      String joinGameCommand = "join 1 BLACK\n";
      outputStream.write(joinGameCommand.getBytes());
      outputStream.flush();

      // Send listGames command
      String listGamesCommand = "listGames\n";
      outputStream.write(listGamesCommand.getBytes());
      outputStream.flush();

      String line;
      StringBuilder serverOutput = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        serverOutput.append(line).append("\n");
      }

      // Close the socket
      reader.close();
      outputStream.close();
      socket.close();

      assertTrue(serverOutput.toString().contains("1: GameData[gameID=1, whiteUsername=null, blackUsername=username, gameName=the greatest game, game=chess.ChessGame@c038203]"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}