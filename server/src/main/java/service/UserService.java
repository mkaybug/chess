package service;

import dataAccess.*;
import model.*;
import model.request.LoginRequest;
import model.request.RegisterRequest;

import java.util.Objects;

public class UserService {
  private final AuthDAO authDataAccess;
  private final UserDAO userDataAccess;

  public UserService(AuthDAO authDataAccess, UserDAO userDataAccess) {
    this.authDataAccess = authDataAccess;
    this.userDataAccess = userDataAccess;
  }

  public AuthData register(RegisterRequest request) throws DataAccessException {
    // If username, password, or email is empty -> throw bad request error
    if (request.username() == null || request.password() == null || request.email() == null) {
      throw new DataAccessException("Error: bad request");
    }

    // Check if user already exists
    UserData existingUser = getUsername(request.username());
    if (existingUser != null) {
      throw new DataAccessException("Error: already taken");
    }
    // Add user to database
    addUser(new UserData(request.username(), request.password(), request.email()));
    // Create auth token, add to database and return it
    AuthData auth = new AuthData(AuthTokenGenerator.generateAuthToken(), request.username());
    authDataAccess.addAuth(auth);
    return auth;
  }
  public AuthData login(LoginRequest request) throws DataAccessException {
    // If username, password, or email is empty -> throw bad request error
    if (request.username() == null || request.password() == null) {
      throw new DataAccessException("Error: unauthorized");
    }

    // Check if user already exists
    try {
      UserData username = getUsername(request.username());
      if (username == null) {
        throw new DataAccessException("Error: unauthorized");
      }
      if (Objects.equals(username.password(), request.password())) {
        // Create auth token, add to database and return it
        AuthData auth = new AuthData(AuthTokenGenerator.generateAuthToken(), request.username());
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

  public void logout(String authToken) throws DataAccessException {
    try {
      // Authenticate user
      AuthData existingAuth = getAuth(authToken);
      if (existingAuth == null) {
        throw new DataAccessException("Error: unauthorized");
      }

      deleteAuthToken(authToken);
    }
    // If auth doesn't exist, throw error
    catch (DataAccessException e) {
      throw new DataAccessException("Error: unauthorized");
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

  public void deleteAuthToken(String authToken) throws DataAccessException {
    authDataAccess.deleteAuthToken(authToken);
  }
}
