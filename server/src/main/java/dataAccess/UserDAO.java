package dataAccess;

import model.UserData;

public interface UserDAO {
  // Insert username, password, email into user
  UserData addUser(UserData user) throws DataAccessException;
  // Select username from user
  UserData getUsername(String username) throws DataAccessException;
  // Delete user
  void deleteUser(String username) throws DataAccessException;
  // Delete all users
  void deleteAllUsers() throws DataAccessException;
}