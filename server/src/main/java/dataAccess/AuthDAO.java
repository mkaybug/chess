// Auth DataAccess Interface
package dataAccess;

import model.AuthData;

public interface AuthDAO {
  // Insert username, authToken into auth
  AuthData addAuth(AuthData auth) throws DataAccessException;
  // Select authToken from auth
  AuthData getAuth(String authToken) throws DataAccessException;
  // Delete authToken from auth
  void deleteAuthToken(String authToken) throws DataAccessException;
  // Delete auth
  void deleteAllAuthTokens() throws DataAccessException;
}