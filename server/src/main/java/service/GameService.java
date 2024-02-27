package service;

import dataAccess.GameDAO;

public class GameService {
  private final GameDAO gameDataAccess;

  public GameService(GameDAO gameDataAccess) {
    this.gameDataAccess = gameDataAccess;
  }
}
