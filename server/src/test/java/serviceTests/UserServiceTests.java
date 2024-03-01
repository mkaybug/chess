package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.AuthData;
import model.UserData;

import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
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
  void register() throws DataAccessException {
    UserData existingUser = new UserData("existingUser", "existingPassword", "existing@gmail.com");

    // Positive test case -> register succeeds
    assertNotNull(userService.register(existingUser));

    // Negative test case -> register fails
    assertThrows(DataAccessException.class, () -> userService.register(existingUser));
  }

  @Test
  void login() throws DataAccessException {
    UserData user = new UserData("user", "password", "email@gmail.com");
    UserData wrongPasswordUser = new UserData("user", "wrongpassword", "email@gmail.com");
    // Negative test case -> login fails
    assertThrows(DataAccessException.class, () -> userService.login(user));

    // Positive test case -> login succeeds
    userService.register(user);
    assertNotNull(userService.login(user));

    // Negative test case -> wrong password, login fails
    assertThrows(DataAccessException.class, () -> userService.login(wrongPasswordUser));
  }

  @Test
  void logout() throws DataAccessException {
    List<AuthData> expected = new ArrayList<>();

    AuthData logoutAuth1 = new AuthData("token3", "username3");
    AuthData logoutAuth2 = new AuthData("token2.1", "username2");

    // Add tokens to the database
    expected.add(userService.addAuth(new AuthData("token1", "username1")));
    expected.add(userService.addAuth(new AuthData("token2", "username2")));

    // Negative test case -> trying to delete an authToken that doesn't exist, fails
    assertThrows(DataAccessException.class, () -> userService.logout(logoutAuth1));
    assertThrows(DataAccessException.class, () -> userService.logout(logoutAuth2));

    // Add the token we just tried to delete
    userService.addAuth(new AuthData("token3", "username3"));

    // Positive test case -> trying to delete authToken again, succeeds
    userService.logout(logoutAuth1);

    var actual = clearService.listAuthTokens();
    assertIterableEquals(expected, actual);
  }

  @Test
  void addUser() throws DataAccessException {
    var user = new UserData("mblack", "school_password", "mblack15@byu.edu");
    user = userService.addUser(user);

    var users = clearService.listUsers();
    assertEquals(1, users.size());
    assertTrue(users.contains(user));
  }
}
