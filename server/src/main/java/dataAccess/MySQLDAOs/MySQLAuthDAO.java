package dataAccess.MySQLDAOs;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.AuthDAO;
import model.AuthData;


import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class MySQLAuthDAO implements AuthDAO {
  private ExecuteUpdate executeUpdate = new ExecuteUpdate();
  public MySQLAuthDAO() throws DataAccessException {
    configureDatabase();
  }

  public AuthData addAuth(AuthData auth) throws DataAccessException {
    var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
    executeUpdate.executeUpdate("auth", statement, auth.authToken(), auth.username());
    return new AuthData(auth.authToken(), auth.username());
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readAuth(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to authenticate");
    }
    return null;
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    var result = new ArrayList<AuthData>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT authToken, username FROM authData";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            result.add(readAuth(rs));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to read auth data");
    }
    return result;
  }

  public void deleteAuthToken(String authToken) throws DataAccessException {
    var statement = "DELETE FROM authData WHERE authToken=?";
    executeUpdate.executeUpdate("auth", statement, authToken);
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var ps = conn.prepareStatement("TRUNCATE TABLE authData");
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error: unable to delete authTokens");
    }
  }

  private AuthData readAuth(ResultSet rs) throws SQLException {
    var authToken = rs.getString("authToken");
    var username = rs.getString("username");

    return new AuthData(authToken, username);
  }

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      createTable(conn);
    } catch (SQLException ex) {
      throw new DataAccessException("Error: unable to configure auth table");
    }
  }

  private static void createTable(Connection connection) throws SQLException {
    String createAuthTableQuery = "CREATE TABLE IF NOT EXISTS authData (" +
            "authToken VARCHAR(50) PRIMARY KEY," +
            "username VARCHAR(50) NOT NULL" +
            ")";

    try (PreparedStatement preparedStatement = connection.prepareStatement(createAuthTableQuery)) {
      preparedStatement.executeUpdate();
    }
  }
}
