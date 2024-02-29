// Auth DataAccess Interface
package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
  // Insert username, authToken into auth
  AuthData addAuth(AuthData auth) throws DataAccessException;
  // Select authToken from auth
  AuthData getAuth(String authToken) throws DataAccessException;
  // Select all authTokens
  Collection<AuthData> listAuthTokens() throws DataAccessException;
  // Delete authToken from auth
  void deleteAuthToken(String authToken) throws DataAccessException;
  // Delete auth
  void deleteAllAuthTokens() throws DataAccessException;
}