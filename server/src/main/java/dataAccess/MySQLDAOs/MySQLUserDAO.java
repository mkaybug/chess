package dataAccess.MySQLDAOs;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class MySQLUserDAO implements UserDAO {
  private ExecuteUpdate executeUpdate = new ExecuteUpdate();
  public MySQLUserDAO() throws DataAccessException {
    configureDatabase();
  }

  public UserData addUser(UserData user) throws DataAccessException {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashedPassword = encoder.encode(user.password());

    var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
    executeUpdate.executeUpdate("user", statement, user.username(), hashedPassword, user.email());
    return new UserData(user.username(), hashedPassword, user.email());
  }

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

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      createTable(conn);
    } catch (SQLException ex) {
      throw new DataAccessException("Error: unable to configure user table");
    }
  }

  private static void createTable(Connection connection) throws SQLException {
    String createUserTableQuery = "CREATE TABLE IF NOT EXISTS userData (" +
            "username VARCHAR(50) PRIMARY KEY," +
            "password VARCHAR(60) NOT NULL," +
            "email VARCHAR(50) NOT NULL" + ")";

    try (PreparedStatement preparedStatement = connection.prepareStatement(createUserTableQuery)) {
      preparedStatement.executeUpdate();
    }
  }
}
