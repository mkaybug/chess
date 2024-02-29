package service;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import model.AuthData;

import java.util.Collection;

public class AuthService {
  private final AuthDAO authDataAccess;

  public AuthService(AuthDAO authDataAccess) {
    this.authDataAccess = authDataAccess;
  }
  // Authenticate user
  public AuthData authenticateUser(AuthData auth) throws DataAccessException {
    // Check if auth already exists
    AuthData existingAuth = getAuth(auth.authToken());
    if (existingAuth == null) {
      throw new DataAccessException("No authentication.");
    }
    else {
      return existingAuth;
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

  public AuthData getAuth(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken);
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return authDataAccess.listAuthTokens();
  }

  public void deleteAuthToken(String authToken) throws DataAccessException {
    authDataAccess.deleteAuthToken(authToken);
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    authDataAccess.deleteAllAuthTokens();
  }
}
