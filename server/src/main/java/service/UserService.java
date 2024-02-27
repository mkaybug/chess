package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Collection;

public class UserService {
  private final UserDAO userDataAccess;

  public UserService(UserDAO userDataAccess) {
    this.userDataAccess = userDataAccess;
  }
  public AuthData register(UserData user) throws DataAccessException {
    return new AuthData("fakeAuth", "username");
  }
  public AuthData login(UserData user) {
    return new AuthData("fakeAuth", "username");
  }
  public void logout(UserData user) {

  }

  public UserData addUser(UserData user) throws DataAccessException {
    return userDataAccess.addUser(user);
  }

  public UserData getUsername(String username) throws DataAccessException {
    return userDataAccess.getUsername(username);
  }

  public void deleteUser(String username) throws DataAccessException {
    userDataAccess.deleteUser(username);
  }

  public void deleteAllUsers() throws DataAccessException {
    userDataAccess.deleteAllUsers();
  }

}
