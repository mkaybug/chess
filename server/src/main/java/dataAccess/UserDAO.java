package dataAccess;

import model.GameData;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
  // Insert username, password, email into user
  UserData addUser(UserData user) throws DataAccessException;
  // Select username from user
  UserData getUsername(String username) throws DataAccessException;
  // Select all games
  Collection<UserData> listUsers() throws DataAccessException;
  // Delete user
  void deleteUser(String username) throws DataAccessException;
  // Delete all users
  void deleteAllUsers() throws DataAccessException;
}