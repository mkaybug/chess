package dataAccess.MySQLDAOs;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLGameDAO implements GameDAO {
  public MySQLGameDAO() throws DataAccessException {
    configureDatabase();
  }
  public GameData addGame(GameData game) throws DataAccessException {
    var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
    executeUpdate(statement, game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    return new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
  }

  public GameData getGame(int gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM gameData WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setInt(1, gameID);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGame(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to get game");
    }
    return null;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    var result = new ArrayList<GameData>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM gameData";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            result.add(readGame(rs));
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to read game data");
    }
    return result;
  }

  public void putGame(GameData game) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      String updateStatement = "UPDATE gameData SET whiteUsername=?, blackUsername=? WHERE gameID=?";
      try (PreparedStatement ps = conn.prepareStatement(updateStatement)) {
        ps.setString(1, game.whiteUsername());
        ps.setString(2, game.blackUsername());
        ps.setInt(3, game.gameID());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
          throw new DataAccessException("No rows updated, game with ID " + game.gameID() + " does not exist.");
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error updating game in the database");
    }
  }

  public void deleteGame(int gameID) throws DataAccessException {
    var statement = "DELETE FROM gameData WHERE gameID=?";
    executeUpdate(statement, gameID);
  }

  public void deleteAllGames() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var ps = conn.prepareStatement("TRUNCATE TABLE gameData");
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Error unable to delete games");
    }
  }

  private GameData readGame(ResultSet rs) throws SQLException {
    var gameID = rs.getInt("gameID");
    var whiteUsername = rs.getString("whiteUsername");
    var blackUsername = rs.getString("blackUsername");
    var gameName = rs.getString("gameName");
    String chessGameJson = rs.getString("chessGame");
    ChessGame chessGame = new Gson().fromJson(chessGameJson, ChessGame.class);

    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
  }

  private void executeUpdate(String statement, Object... params) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(statement)) {
        for (int i = 0; i < params.length; i++) {
          if (params[i] instanceof String) {
            ps.setString(i + 1, (String) params[i]);
          } else if (params[i] instanceof Integer) {
            ps.setInt(i + 1, (Integer) params[i]);
          } else if (params[i] instanceof ChessGame p) {
            // Serialize ChessGame object to Json object
            String chessGameJson = new Gson().toJson(p);

            ps.setString(i + 1, chessGameJson);
          }
        }
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("Error: unable to update database with game info");
    }
  }


  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      createTable(conn);
    } catch (SQLException ex) {
      throw new DataAccessException("Error: unable to configure game table");
    }
  }

  public static void createTable(Connection connection) throws SQLException {
    String CREATE_GAME_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS gameData (" +
            "gameID INT PRIMARY KEY," +
            "whiteUsername VARCHAR(50)," +
            "blackUsername VARCHAR(50)," +
            "gameName VARCHAR(50) NOT NULL," +
            "chessGame VARCHAR(3000) NOT NULL" +
            ")";

    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_GAME_TABLE_QUERY)) {
      preparedStatement.executeUpdate();
      System.out.println("Table 'gameData' created successfully.");
    }
  }
}
