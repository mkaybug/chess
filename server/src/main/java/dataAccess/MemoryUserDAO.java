// Data Access for user table using in memory storage (RAM)
package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
  private final HashMap<String, UserData> userDataMap = new HashMap<>();

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

  // Delete user using username
  public void deleteUser(String username) {
    userDataMap.remove(username);
  }

  // Clear userDataMap
  public void deleteAllUsers() {
    userDataMap.clear();
  }
}
