package dataAccess.MySQLDAOs;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQLUserDAO implements UserDAO {
  public void MySqlUserDAO() throws DataAccessException {
    configureDatabase();
  }
  public UserData addUser(UserData user) throws DataAccessException {
    var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
    executeUpdate(statement, user.username(), user.password(), user.email());
    return new UserData(user.username(), user.password(), user.email());
  }

  // FIXME Where I left off: I think that the table is not getting created, or not instantiated.
  // FIXME Perhaps press pause here and test listUsers to see if we have anything.
  public UserData getUsername(String username) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM userData WHERE username=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readUser(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to get user data");
    }
    return null;
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    var result = new ArrayList<UserData>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM userData";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            result.add(readUser(rs));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to read user data");
    }
    return result;
  }

  public void deleteAllUsers() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var ps = conn.prepareStatement("TRUNCATE TABLE userData");
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error: unable to update database with user info");
    }
  }

  private UserData readUser(ResultSet rs) throws SQLException {
    var username = rs.getString("username");
    var password = rs.getString("password");
    var email = rs.getString("email");

    return new UserData(username, password, email);
  }

  private void executeUpdate(String statement, String... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement)) {
        for (var i = 0; i < params.length; i++) {
          ps.setString(i + 1, params[i]);
        }
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error: unable to update database with user info");
    }
  }

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      createTable(conn);
    } catch (SQLException ex) {
      throw new DataAccessException("Error: unable to configure user table");
    }
  }

  public static void createTable(Connection connection) throws SQLException {
    String CREATE_USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS userData (" +
            "username VARCHAR(50) PRIMARY KEY," +
            "password VARCHAR(50) NOT NULL," +
            "email VARCHAR(50) NOT NULL" + ")";

    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER_TABLE_QUERY)) {
      preparedStatement.executeUpdate();
      System.out.println("Table 'userData' created successfully.");
    }
  }
}
