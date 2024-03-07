package dataAccess.MySQLDAOs;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

import java.util.Collection;

public class MySQLAuthDAO implements UserDAO {
  public UserData addUser(UserData user) throws DataAccessException {
    return null;
  }

  public UserData getUsername(String username) throws DataAccessException {
    return null;
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return null;
  }

  public void deleteAllUsers() throws DataAccessException {

  }
}
