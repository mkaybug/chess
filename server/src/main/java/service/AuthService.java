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

  public AuthData addAuth(AuthData auth) throws DataAccessException {
    return authDataAccess.addAuth(auth);
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken);
  }

  public void deleteAuthToken(String authToken) throws DataAccessException {
    authDataAccess.deleteAuthToken(authToken);
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    authDataAccess.deleteAllAuthTokens();
  }
}
