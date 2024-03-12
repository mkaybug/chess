package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLUserDAO;
import model.UserData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

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
    UserData user1 = new UserData ("username", "password", "email");
    UserData user2 = new UserData ("username", "password", "email");

    mySQLUserDAO.addUser(user1);
    assertThrows(DataAccessException.class, () -> mySQLUserDAO.addUser(user2));
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
    assertNull(mySQLUserDAO.getUsername("username"));
  }

  @Test
  @DisplayName("Get User Success")
  void getUserSuccess() throws DataAccessException {
    UserData user = new UserData ("Makayla", "this_password", "email@gmail.com");
    mySQLUserDAO.addUser(user);

    assertEquals(user, mySQLUserDAO.getUsername(user.username()));
  }

  @Test
  @DisplayName("List Users Success")
  void listUsersSuccess() throws DataAccessException {
    ArrayList<UserData> expected = new ArrayList<>();

    UserData user1 = new UserData ("username1", "password1", "email1");
    UserData user2 = new UserData ("username2", "password2", "email2");
    UserData user3 = new UserData ("username3", "password3", "email3");
    expected.add(mySQLUserDAO.addUser(user1));
    expected.add(mySQLUserDAO.addUser(user2));
    expected.add(mySQLUserDAO.addUser(user3));

    assertIterableEquals(expected, mySQLUserDAO.listUsers());
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

    mySQLUserDAO.deleteAllUsers();
    Collection<UserData> actual = mySQLUserDAO.listUsers();

    assertEquals(0, actual.size());
  }
}
