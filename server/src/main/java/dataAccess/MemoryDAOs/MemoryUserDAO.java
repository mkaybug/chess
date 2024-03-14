// Data Access for user table using in memory storage (RAM)
package dataAccess.MemoryDAOs;

import dataAccess.UserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MemoryUserDAO implements UserDAO {
  private final HashMap<String, UserData> userDataMap = new LinkedHashMap<>();

  // Insert user (username, password, and email)
  public UserData addUser(UserData user) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(user.password());

    user = new UserData(user.username(), hashedPassword, user.email());

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
