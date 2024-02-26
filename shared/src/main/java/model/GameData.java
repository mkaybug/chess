package model;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
  // Additional methods or constructors can be added here if needed
}
