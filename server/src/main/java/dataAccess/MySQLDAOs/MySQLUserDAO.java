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
import static java.sql.Types.NULL;

public class MySQLUserDAO implements UserDAO {

  public void MySqlUserDAO() throws DataAccessException {
    configureDatabase();
  }
  public UserData addUser(UserData user) throws DataAccessException {
    var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
    var json = new Gson().toJson(user);
    var username = executeUpdate(statement, user.username(), user.password(), user.email(), json);
    return new UserData(user.username(), user.password(), user.email());
  }

  public UserData getUsername(String username) throws DataAccessException {
    return null;
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return null;
  }

  public void deleteAllUsers() throws DataAccessException {

  }

  private int executeUpdate(String statement, Object... params) throws ResponseException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (SQLException e) {
      throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };


  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }
}
