package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
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
    RegisterRequest newUser = new RegisterRequest("user", "password", "email@gmail.com");
    RegisterRequest nullUser = new RegisterRequest(null, "password", "email@gmail.com");
    RegisterRequest nullPassword = new RegisterRequest("user", null, "email@gmail.com");
    RegisterRequest nullEmail = new RegisterRequest("user", "password", null);

    // Negative test case -> username null
    assertThrows(DataAccessException.class, () -> userService.register(nullUser));

    // Negative test case -> password null
    assertThrows(DataAccessException.class, () -> userService.register(nullPassword));

    // Negative test case -> email null
    assertThrows(DataAccessException.class, () -> userService.register(nullEmail));

    // Positive test case -> register succeeds
    assertNotNull(userService.register(newUser));

    // Negative test case -> register fails
    assertThrows(DataAccessException.class, () -> userService.register(newUser));
  }

  @Test
  void login() throws DataAccessException {
    LoginRequest user = new LoginRequest("user", "password");
    LoginRequest wrongPasswordUser = new LoginRequest("user", "wrongPassword");
    LoginRequest nullUser = new LoginRequest(null, "password");
    LoginRequest nullPassword = new LoginRequest("user", null);

    // Negative test case -> user doesn't exist, login fails
    assertThrows(DataAccessException.class, () -> userService.login(user));

    // Positive test case -> login succeeds
    userService.register(new RegisterRequest("user", "password", "email@gmail.com"));
    assertNotNull(userService.login(user));

    // Negative test case -> wrong password, login fails
    assertThrows(DataAccessException.class, () -> userService.login(wrongPasswordUser));

    // Negative test case -> username null
    assertThrows(DataAccessException.class, () -> userService.login(nullUser));

    // Negative test case -> password null
    assertThrows(DataAccessException.class, () -> userService.login(nullPassword));
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
