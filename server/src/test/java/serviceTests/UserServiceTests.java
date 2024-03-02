package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;

import model.AuthData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
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
  @DisplayName("Register Success")
  void registerSucceeds() throws DataAccessException {
    RegisterRequest newUser = new RegisterRequest("user", "password", "email@gmail.com");

    // Positive test case -> register succeeds
    assertNotNull(userService.register(newUser));
  }

  @Test
  @DisplayName("Register User Already Taken")
  void registerFails() throws DataAccessException {
    RegisterRequest newUser = new RegisterRequest("user", "password", "email@gmail.com");

    userService.register(newUser);

    // Negative test case -> register fails
    assertThrows(DataAccessException.class, () -> userService.register(newUser));
  }

  @Test
  @DisplayName("Register Null Fields")
  void registerNullFields() throws DataAccessException {
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
  }

  @Test
  @DisplayName("Login Success")
  void loginSuccess() throws DataAccessException {
    LoginRequest user = new LoginRequest("user", "password");

    // Positive test case -> login succeeds
    userService.register(new RegisterRequest("user", "password", "email@gmail.com"));
    assertNotNull(userService.login(user));
  }

  @Test
  @DisplayName("Login User Doesn't Exist")
  void loginNoExistingUser() throws DataAccessException {
    LoginRequest user = new LoginRequest("user", "password");

    // Negative test case -> user doesn't exist, login fails
    assertThrows(DataAccessException.class, () -> userService.login(user));
  }

  @Test
  @DisplayName("Login Wrong Password")
  void loginWrongPassword() throws DataAccessException {
    LoginRequest wrongPasswordUser = new LoginRequest("user", "wrongPassword");

    // Negative test case -> wrong password, login fails

    userService.register(new RegisterRequest("user", "password", "email@gmail.com"));
    assertThrows(DataAccessException.class, () -> userService.login(wrongPasswordUser));
  }

  @Test
  @DisplayName("Login Fails Null Field")
  void loginNullField() throws DataAccessException {
    LoginRequest user = new LoginRequest("user", "password");
    LoginRequest wrongPasswordUser = new LoginRequest("user", "wrongPassword");
    LoginRequest nullUser = new LoginRequest(null, "password");
    LoginRequest nullPassword = new LoginRequest("user", null);

    userService.register(new RegisterRequest("user", "password", "email@gmail.com"));

    // Negative test case -> username null
    assertThrows(DataAccessException.class, () -> userService.login(nullUser));

    // Negative test case -> password null
    assertThrows(DataAccessException.class, () -> userService.login(nullPassword));
  }

  @Test
  @DisplayName("Logout Success")
  void logoutSuccess() throws DataAccessException {
    List<AuthData> expected = new ArrayList<>();

    AuthData logoutAuth1 = new AuthData("token3", "username3");

    // Add tokens to the database
    expected.add(userService.addAuth(new AuthData("token1", "username1")));
    expected.add(userService.addAuth(new AuthData("token2", "username2")));

    // Add the token we will try to delete
    userService.addAuth(new AuthData("token3", "username3"));

    // Positive test case -> trying to delete authToken again, succeeds
    userService.logout(logoutAuth1.authToken());

    var actual = clearService.listAuthTokens();
    assertIterableEquals(expected, actual);
  }

  @Test
  @DisplayName("Logout Fails")
  void logoutFails() throws DataAccessException {
    AuthData logoutAuth1 = new AuthData("token3", "username3");
    AuthData logoutAuth2 = new AuthData("token2.1", "username2");

    // Add tokens to the database
    userService.addAuth(new AuthData("token1", "username1"));
    userService.addAuth(new AuthData("token2", "username2"));

    // Negative test case -> trying to delete an authToken that doesn't exist, fails
    assertThrows(DataAccessException.class, () -> userService.logout(logoutAuth1.authToken()));
    assertThrows(DataAccessException.class, () -> userService.logout(logoutAuth2.authToken()));
  }

  @Test
  @DisplayName("Add User")
  void addUser() throws DataAccessException {
    var user = new UserData("mblack", "school_password", "mblack15@byu.edu");
    user = userService.addUser(user);

    var users = clearService.listUsers();
    assertEquals(1, users.size());
    assertTrue(users.contains(user));
  }
}
