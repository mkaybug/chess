// Data Access for auth table using in memory storage (RAM)
package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
  private final HashMap<String, AuthData> authDataMap = new HashMap<>();

  // Insert auth data (username and authToken)
  public AuthData addAuth(AuthData auth) {
    auth = new AuthData(auth.authToken(), auth.username());

    authDataMap.put(auth.authToken(), auth);
    return auth;
  }

  // Select auth data using authToken
  public AuthData getAuth(String authToken) {
    return authDataMap.get(authToken);
  }

  public Collection<AuthData> listAuthTokens() {
    return authDataMap.values();
  }

  // Delete auth data using authToken
  public void deleteAuthToken(String authToken) {
    authDataMap.remove(authToken);
  }

  // Clear authDataMap (auth table
  public void deleteAllAuthTokens() {
    authDataMap.clear();
  }
}
