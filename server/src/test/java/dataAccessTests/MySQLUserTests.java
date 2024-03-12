package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLUserDAO;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySQLUserTests {
  MySQLUserDAO mySQLUserDAO = null;

  @BeforeEach
  void setUp() throws DataAccessException {
    try {
      mySQLUserDAO = new MySQLUserDAO();
      mySQLUserDAO.deleteAllUsers();
      mySQLUserDAO.MySqlUserDAO();
    }
    catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  @Test
  @DisplayName("Add User Fail")
  void addUserFail() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> mySQLUserDAO.getUsername("this_username"));
  }

  @Test
  @DisplayName("Add User Success")
  void addUserSuccess() throws DataAccessException {
    UserData user1 = new UserData ("username1", "password1", "email1@gmail.com");
    UserData user2 = new UserData ("username2", "password2", "email2@gmail.com");
    UserData user3 = new UserData ("username3", "password3", "email3@gmail.com");

    mySQLUserDAO.addUser(user1);
    mySQLUserDAO.addUser(user2);
    mySQLUserDAO.addUser(user3);

    assertEquals(user1, mySQLUserDAO.getUsername(user1.username()));
    assertEquals(user2, mySQLUserDAO.getUsername(user2.username()));
    assertEquals(user3, mySQLUserDAO.getUsername(user3.username()));
  }

  @Test
  @DisplayName("Get User Fail")
  void getUserFail() throws DataAccessException {
    assertThrows(DataAccessException.class, () -> mySQLUserDAO.getUsername("username"));
  }

  @Test
  @DisplayName("Get User Success")
  void getUserSuccess() throws DataAccessException {
    UserData user = new UserData ("Makayla", "this_password", "email@gmail.com");
    mySQLUserDAO.addUser(user);

    assertEquals(user, mySQLUserDAO.getUsername(user.username()));
  }

  @Test
  @DisplayName("List Users")
  void listUsers() throws DataAccessException {
    ArrayList<UserData> expected = new ArrayList<>();

    UserData user1 = new UserData ("username", "password", "email");
    UserData user2 = new UserData ("username", "password", "email");
    UserData user3 = new UserData ("username", "password", "email");
    expected.add(mySQLUserDAO.addUser(user1));
    expected.add(mySQLUserDAO.addUser(user2));
    expected.add(mySQLUserDAO.addUser(user3));

    assertIterableEquals(expected, mySQLUserDAO.listUsers());
  }

  @Test
  @DisplayName("Read User")
  void readUser() throws DataAccessException {
  }

  @Test
  @DisplayName("Delete All Users")
  void deleteAllUsers() throws DataAccessException {
    UserData user1 = new UserData ("username1", "password1", "email1@gmail.com");
    UserData user2 = new UserData ("username2", "password2", "email2@gmail.com");
    UserData user3 = new UserData ("username3", "password3", "email3@gmail.com");

    mySQLUserDAO.addUser(user1);
    mySQLUserDAO.addUser(user2);
    mySQLUserDAO.addUser(user3);

//    mySQLUserDAO.deleteAllUsers();
    Collection<UserData> actual = mySQLUserDAO.listUsers();

    assertEquals(0, actual.size());
  }
}
