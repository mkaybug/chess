package dataAccess.MySQLDAOs;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class ExecuteUpdate {
  public void executeUpdate(String type, String statement, String... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement)) {
        for (var i = 0; i < params.length; i++) {
          ps.setString(i + 1, params[i]);
        }
        ps.executeUpdate();
      }
    } catch (SQLException e) {
        throw new DataAccessException(String.format("Error: unable to update database with %s info", type));
    }
  }
}
