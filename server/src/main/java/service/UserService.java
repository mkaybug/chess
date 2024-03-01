package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
  private final AuthDAO authDataAccess;
  private final GameDAO gameDataAccess;
  private final UserDAO userDataAccess;

  public UserService(AuthDAO authDataAccess, GameDAO gameDataAccess, UserDAO userDataAccess) {
    this.authDataAccess = authDataAccess;
    this.gameDataAccess = gameDataAccess;
    this.userDataAccess = userDataAccess;
  }

  public AuthData authenticateUser(AuthData auth) throws DataAccessException {
    // Check if auth already exists
    AuthData existingAuth = getAuth(auth.authToken());
    if (existingAuth == null) {
      throw new DataAccessException("Error: unauthorized");
    }
    else {
      return existingAuth;
    }
  }

  public AuthData register(UserData user) throws DataAccessException {
    // Check if user already exists
    UserData existingUser = getUsername(user.username());
    if (existingUser != null) {
      throw new DataAccessException("Error: already taken");
    }
    // Add user to database
    addUser(user);
    // Create auth token, add to database and return it
    AuthData auth = new AuthData(AuthTokenGenerator.generateAuthToken(), user.username());
    authDataAccess.addAuth(auth);
    return auth;
  }
  public AuthData login(UserData user) throws DataAccessException {
    // Check if user already exists
    try {
      UserData username = getUsername(user.username());
      if (username == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      if (Objects.equals(username.password(), user.password())) {
        // Create auth token, add to database and return it
        AuthData auth = new AuthData(AuthTokenGenerator.generateAuthToken(), user.username());
        authDataAccess.addAuth(auth);
        return auth;
      }
      else {
        throw new DataAccessException("Error: unauthorized");
      }
    }
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
    }
  }

  public void logout(AuthData auth) throws DataAccessException {
    // Authenticate user
    try {
      authenticateUser(auth);
      deleteAuthToken(auth.authToken());
    }
    // If auth doesn't exist, throw error
    catch (DataAccessException e) {
      throw new DataAccessException("Already logged out.");
    }
  }

  public AuthData addAuth(AuthData auth) throws DataAccessException {
    return authDataAccess.addAuth(auth);
  }

  public UserData addUser(UserData user) throws DataAccessException {
    return userDataAccess.addUser(user);
  }

  public UserData getUsername(String username) throws DataAccessException {
    return userDataAccess.getUsername(username);
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken);
  }

  public void deleteUser(String username) throws DataAccessException {
    userDataAccess.deleteUser(username);
  }

  public void deleteAuthToken(String authToken) throws DataAccessException {
    authDataAccess.deleteAuthToken(authToken);
  }
}
