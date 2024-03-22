package clientTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLAuthDAO;
import dataAccess.MySQLDAOs.MySQLGameDAO;
import dataAccess.MySQLDAOs.MySQLUserDAO;
import service.ClearService;

import org.junit.jupiter.api.*;
import server.Server;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerFacadeTests {

  private static Server server;
  ClearService clearService = null;

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
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
  void testRegisterCommand() {
    try {
      // Connect to localhost on the port your server is running
      Socket socket = new Socket("localhost", 8080); // Assuming your server is running on port 8080
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Send the register command
      String registerCommand = "register username password email@gmail.com\n"; // Add '\n' to simulate pressing enter
      outputStream.write(registerCommand.getBytes());
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

      // Assert some condition to check if registration was successful
      assertTrue(serverOutput.toString().contains("Registration successful, login to play."));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testLoginCommand() {
    try {
      // Connect to localhost on the port your server is running
      Socket socket = new Socket("localhost", 8080); // Assuming your server is running on port 8080
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Send the register command
      String registerCommand = "register username password email@gmail.com\n"; // Add '\n' to simulate pressing enter
      outputStream.write(registerCommand.getBytes());
      outputStream.flush();

      // Send the login command
      String loginCommand = "login username password\n"; // Add '\n' to simulate pressing enter
      outputStream.write(loginCommand.getBytes());
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

      // Assert some condition to check if registration was successful
      assertTrue(serverOutput.toString().contains("Welcome username! Type help to begin playing."));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testLogoutCommand() {
    try {
      // Connect to localhost on the port your server is running
      Socket socket = new Socket("localhost", 8080); // Assuming your server is running on port 8080
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Send register command
      String registerCommand = "register username password email@gmail.com\n"; // Add '\n' to simulate pressing enter
      outputStream.write(registerCommand.getBytes());
      outputStream.flush();

      // Send login command
      String loginCommand = "login username password\n"; // Add '\n' to simulate pressing enter
      outputStream.write(loginCommand.getBytes());
      outputStream.flush();

      // Send logout command
      String logoutCommand = "logout\n"; // Add '\n' to simulate pressing enter
      outputStream.write(logoutCommand.getBytes());
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

      // Assert some condition to check if registration was successful
      assertTrue(serverOutput.toString().contains("You successfully logged out."));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testCreateGameCommand() {
    try {
      // Connect to localhost on the port your server is running
      Socket socket = new Socket("localhost", 8080); // Assuming your server is running on port 8080
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      // Send register command
      String registerCommand = "register username password email@gmail.com\n"; // Add '\n' to simulate pressing enter
      outputStream.write(registerCommand.getBytes());
      outputStream.flush();

      // Send login command
      String loginCommand = "login username password\n"; // Add '\n' to simulate pressing enter
      outputStream.write(loginCommand.getBytes());
      outputStream.flush();

      // Send createGame command
      String createGameCommand = "createGame the greatest game\n"; // Add '\n' to simulate pressing enter
      outputStream.write(createGameCommand.getBytes());
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

      // Assert some condition to check if registration was successful
      assertTrue(serverOutput.toString().contains("You successfully logged out."));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}