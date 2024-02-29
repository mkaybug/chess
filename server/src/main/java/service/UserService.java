package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.AuthService;

import java.util.Collection;

public class UserService {
  private final UserDAO userDataAccess;

  public UserService(UserDAO userDataAccess) {
    this.userDataAccess = userDataAccess;
  }

  // FIXME register() and login() need to add authTokens to the database, not just return them
  public AuthData register(UserData user) throws DataAccessException {
    // Check if user already exists
    UserData existingUser = getUsername(user.username());
    if (existingUser != null) {
      throw new DataAccessException("User already exists.");
    }

    addUser(user);
    return new AuthData(AuthTokenGenerator.generateAuthToken(), user.username());
  }
  public AuthData login(UserData user) throws DataAccessException {
    // Check if user already exists
    UserData existingUser = getUsername(user.username());
    if (existingUser == null) {
      throw new DataAccessException("User doesn't exist.");
    }
    else {
      return new AuthData(AuthTokenGenerator.generateAuthToken(), user.username());
    }
  }

  public UserData addUser(UserData user) throws DataAccessException {
    return userDataAccess.addUser(user);
  }

  public UserData getUsername(String username) throws DataAccessException {
    return userDataAccess.getUsername(username);
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return userDataAccess.listUsers();
  }

  public void deleteUser(String username) throws DataAccessException {
    userDataAccess.deleteUser(username);
  }

  public void deleteAllUsers() throws DataAccessException {
    userDataAccess.deleteAllUsers();
  }

}
