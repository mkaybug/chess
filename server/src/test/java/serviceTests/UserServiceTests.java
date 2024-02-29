package serviceTests;

import dataAccess.MemoryUserDAO;
import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
  static final UserService userService = new UserService(new MemoryUserDAO());

  // Setup
  @BeforeEach
  void clear() throws DataAccessException {
    userService.deleteAllUsers();
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
    UserData existingUser = new UserData("existingUser", "existingPassword", "existing@gmail.com");
    // Negative test case -> login fails
    assertThrows(DataAccessException.class, () -> userService.login(existingUser));

    // Positive test case -> login succeeds
    userService.register(existingUser);
    assertNotNull(userService.login(existingUser));
  }

  @Test
  void addUser() throws DataAccessException {
    var user = new UserData("mblack", "school_password", "mblack15@byu.edu");
    user = userService.addUser(user);

    var users = userService.listUsers();
    assertEquals(1, users.size());
    assertTrue(users.contains(user));
  }

  @Test
  void listUsers() throws DataAccessException {
    List<UserData> expected = new ArrayList<>();
    expected.add(userService.addUser(new UserData("mblack", "school_password", "mblack15@byu.edu")));
    expected.add(userService.addUser(new UserData("this_username", "this_password", "thisEmail@byu.edu")));
    expected.add(userService.addUser(new UserData("lastUser", "lastPassword", "CS240@byu.edu")));

    var actual = userService.listUsers();
    assertIterableEquals(expected, actual);
  }

  @Test
  void deleteUser() throws DataAccessException {
    List<UserData> expected = new ArrayList<>();
    var user = userService.addUser(new UserData("mblack", "school_password", "mblack15@byu.edu"));
    expected.add(userService.addUser(new UserData("this_username", "this_password", "thisEmail@byu.edu")));
    expected.add(userService.addUser(new UserData("lastUser", "lastPassword", "CS240@byu.edu")));

    userService.deleteUser(user.username());
    var actual = userService.listUsers();
    assertIterableEquals(expected, actual);
  }

  @Test
  void deleteAllUsers() throws DataAccessException {
    userService.addUser(new UserData("mblack", "school_password", "mblack15@byu.edu"));
    userService.addUser(new UserData("this_username", "this_password", "thisEmail@byu.edu"));
    userService.addUser(new UserData("lastUser", "lastPassword", "CS240@byu.edu"));

    userService.deleteAllUsers();
    assertEquals(0, userService.listUsers().size());
  }
}
