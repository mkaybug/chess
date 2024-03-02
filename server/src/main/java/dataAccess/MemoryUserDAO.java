// Data Access for user table using in memory storage (RAM)
package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MemoryUserDAO implements UserDAO {
  private final HashMap<String, UserData> userDataMap = new LinkedHashMap<>();

  // Insert user (username, password, and email)
  public UserData addUser(UserData user) {
    user = new UserData(user.username(), user.password(), user.email());

    userDataMap.put(user.username(), user);
    return user;
  }

  // Select user using username
  public UserData getUsername(String username) {
    return userDataMap.get(username);
  }

  // Select all games
  public Collection<UserData> listUsers() {
    return userDataMap.values();
  }

  // Clear userDataMap
  public void deleteAllUsers() {
    userDataMap.clear();
  }
}
