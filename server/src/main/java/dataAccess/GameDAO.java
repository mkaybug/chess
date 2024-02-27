package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
  // Insert gameName into game
  GameData addGame(GameData game) throws DataAccessException;
  // Select game
  GameData getGame(int gameID) throws DataAccessException;
  // Select all games
  Collection<GameData> listGames() throws DataAccessException;
  // Put game
  void putGame(GameData game) throws DataAccessException;
  // Delete game
  void deleteGame(int gameID) throws DataAccessException;
  // Delete all games
  void deleteAllGames() throws DataAccessException;
}