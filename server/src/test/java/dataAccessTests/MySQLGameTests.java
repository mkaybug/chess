package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MySQLDAOs.MySQLGameDAO;
import model.GameData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameTests {
  MySQLGameDAO mySQLGameDAO = null;

  @BeforeEach
  void setUp() throws DataAccessException {
    try {
      mySQLGameDAO = new MySQLGameDAO();
      mySQLGameDAO.deleteAllGames();
    }
    catch (DataAccessException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  @Test
  @DisplayName("Add Game Fail")
  void addGameFail() throws DataAccessException {
    GameData game1 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    GameData game2 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());

    mySQLGameDAO.addGame(game1);
    assertThrows(DataAccessException.class, () -> mySQLGameDAO.addGame(game2));
  }

  @Test
  @DisplayName("Add Game Success")
  void addGameSuccess() throws DataAccessException {
    GameData game1 = new GameData (1, "white_username1", "black_username1", "game_name1", new ChessGame());
    GameData game2 = new GameData (2, "white_username2", "black_username2", "game_name2", new ChessGame());
    GameData game3 = new GameData (3, "white_username3", "black_username3", "game_name3", new ChessGame());

    mySQLGameDAO.addGame(game1);
    mySQLGameDAO.addGame(game2);
    mySQLGameDAO.addGame(game3);

    assertEquals(game1.whiteUsername(), mySQLGameDAO.getGame(game1.gameID()).whiteUsername());
    assertEquals(game1.blackUsername(), mySQLGameDAO.getGame(game1.gameID()).blackUsername());
    assertEquals(game1.gameName(), mySQLGameDAO.getGame(game1.gameID()).gameName());
    assertTrue(mySQLGameDAO.getGame(game1.gameID()).game() instanceof ChessGame);
    assertEquals(game2.whiteUsername(), mySQLGameDAO.getGame(game2.gameID()).whiteUsername());
    assertEquals(game2.blackUsername(), mySQLGameDAO.getGame(game2.gameID()).blackUsername());
    assertEquals(game2.gameName(), mySQLGameDAO.getGame(game2.gameID()).gameName());
    assertTrue(mySQLGameDAO.getGame(game2.gameID()).game() instanceof ChessGame);
    assertEquals(game3.whiteUsername(), mySQLGameDAO.getGame(game3.gameID()).whiteUsername());
    assertEquals(game3.blackUsername(), mySQLGameDAO.getGame(game3.gameID()).blackUsername());
    assertEquals(game3.gameName(), mySQLGameDAO.getGame(game3.gameID()).gameName());
    assertTrue(mySQLGameDAO.getGame(game3.gameID()).game() instanceof ChessGame);
  }

  @Test
  @DisplayName("Get Game Fail")
  void getGameFail() throws DataAccessException {
    assertNull(mySQLGameDAO.getGame(1));
  }

  @Test
  @DisplayName("Get Game Success")
  void getGameSuccess() throws DataAccessException {
    GameData game = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    mySQLGameDAO.addGame(game);

    assertEquals(game.whiteUsername(), mySQLGameDAO.getGame(game.gameID()).whiteUsername());
    assertEquals(game.blackUsername(), mySQLGameDAO.getGame(game.gameID()).blackUsername());
    assertEquals(game.gameName(), mySQLGameDAO.getGame(game.gameID()).gameName());
    assertTrue(mySQLGameDAO.getGame(game.gameID()).game() instanceof ChessGame);
  }

  @Test
  @DisplayName("List Game Success")
  void listGames() throws DataAccessException {
    ArrayList<GameData> expected = new ArrayList<>();

    GameData game1 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    GameData game2 = new GameData (2, "white_username", "black_username", "game_name", new ChessGame());
    GameData game3 = new GameData (3, "white_username", "black_username", "game_name", new ChessGame());

    expected.add(mySQLGameDAO.addGame(game1));
    expected.add(mySQLGameDAO.addGame(game2));
    expected.add(mySQLGameDAO.addGame(game3));

    assertEquals(3, mySQLGameDAO.listGames().size());
  }

  @Test
  @DisplayName ("Update Game Success")
  void updateGameSuccess() throws DataAccessException {
    ChessGame chessGame = new ChessGame();
    GameData game = new GameData (1, "white_username", "", "game_name", chessGame);
    mySQLGameDAO.addGame(game);

    GameData updatedGame = new GameData (1, "white_username", "black_username", "game_name", chessGame);
    mySQLGameDAO.putGame(updatedGame);

    assertEquals(updatedGame.whiteUsername(), mySQLGameDAO.getGame(updatedGame.gameID()).whiteUsername());
    assertEquals(updatedGame.blackUsername(), mySQLGameDAO.getGame(updatedGame.gameID()).blackUsername());
  }

  @Test
  @DisplayName ("Update Game Fail")
  void updateGameFail() throws DataAccessException {
    ChessGame chessGame = new ChessGame();
    GameData game = new GameData (1, "white_username", "", "game_name", chessGame);
    mySQLGameDAO.addGame(game);

    GameData updatedGame = new GameData (2, "white_username", "black_username", "game_name", chessGame);
    assertThrows(DataAccessException.class, () -> mySQLGameDAO.putGame(updatedGame));
  }

  @Test
  @DisplayName("Delete Game Success")
  void deleteGameSuccess() throws DataAccessException {

    GameData game1 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    GameData game2 = new GameData (2, "white_username", "black_username", "game_name", new ChessGame());
    GameData game3 = new GameData (3, "white_username", "black_username", "game_name", new ChessGame());

    mySQLGameDAO.addGame(game1);
    mySQLGameDAO.addGame(game2);
    mySQLGameDAO.addGame(game3);

    mySQLGameDAO.deleteGame(game1.gameID());
    Collection<GameData> actual = mySQLGameDAO.listGames();

    assertEquals(2, actual.size());
  }

  @Test
  @DisplayName("Delete Game Fail")
  void deleteGameFail() throws DataAccessException {
    GameData game1 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    GameData game2 = new GameData (2, "white_username", "black_username", "game_name", new ChessGame());
    GameData game3 = new GameData (3, "white_username", "black_username", "game_name", new ChessGame());

    mySQLGameDAO.addGame(game1);
    mySQLGameDAO.addGame(game2);
    mySQLGameDAO.addGame(game3);

    mySQLGameDAO.deleteGame(4);
    Collection<GameData> actual = mySQLGameDAO.listGames();

    assertEquals(3, actual.size());
  }

  @Test
  @DisplayName("Delete All Games")
  void deleteAllGames() throws DataAccessException {
    GameData game1 = new GameData (1, "white_username", "black_username", "game_name", new ChessGame());
    GameData game2 = new GameData (2, "white_username", "black_username", "game_name", new ChessGame());
    GameData game3 = new GameData (3, "white_username", "black_username", "game_name", new ChessGame());

    mySQLGameDAO.addGame(game1);
    mySQLGameDAO.addGame(game2);
    mySQLGameDAO.addGame(game3);

    mySQLGameDAO.deleteAllGames();
    Collection<GameData> actual = mySQLGameDAO.listGames();

    assertEquals(0, actual.size());
  }
}
