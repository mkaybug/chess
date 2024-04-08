package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

import static ui.EscapeSequences.*;

public class PrintChessBoard {
  ChessBoard chessBoard;
  String teamColor;


  public PrintChessBoard(ChessBoard chessBoard, String teamColor) {
    this.chessBoard = chessBoard;
    this.teamColor = teamColor;
  }

  public String printBoard() {
    if (Objects.equals(teamColor, "WHITE")) {
      return printWhiteTeamBoard();
    }
    else if (Objects.equals(teamColor , "BLACK")) {
      return printBlackTeamBoard();
    }
    else {
      return printWhiteTeamBoard();
    }
  }

  public String printWhiteTeamBoard() {
    StringBuilder board = new StringBuilder();
    board.append("\n\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n");

    for (int i = 8; i > 0; i--) {
      boolean whiteSpace = i % 2 == 0;
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" ");

      for (int j = 8; j > 0; j--) {
        whiteSpace = createSquareString(board, i, whiteSpace, j);
      }

      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" \u001B[0m\n");
    }

    board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n\n");
    return String.valueOf(board);
  }

  public String printBlackTeamBoard() {
    StringBuilder board = new StringBuilder();
    board.append("\n\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    \u001B[0m\n");

    for (int i = 1; i < 9; i++) {
      boolean whiteSpace = i % 2 != 0;
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" ");

      for (int j = 1; j < 9; j++) {
        whiteSpace = createSquareString(board, i, whiteSpace, j);
      }
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" \u001B[0m\n");
    }

    board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    \u001B[0m\n\n");
    return String.valueOf(board);
  }

  private boolean createSquareString(StringBuilder board, int i, boolean whiteSpace, int j) {
    if (whiteSpace) {
      board.append(SET_BG_COLOR_WHITE);
      whiteSpace = false;
    }
    else {
      board.append(SET_BG_COLOR_DARK_GREY);
      whiteSpace = true;
    }

    ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));

    if (piece != null) {
      board.append(createPieceString(piece));
    }
    else {
      board.append("   ");
    }
    return whiteSpace;
  }

  private String createPieceString(ChessPiece piece) {
    String square;

    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
      square = SET_TEXT_COLOR_BLUE;
    }
    else {
      square = SET_TEXT_COLOR_RED;
    }

    switch (piece.getPieceType()) {
      case ROOK:
        return square + " R ";
      case KNIGHT:
        return square + " N ";
      case BISHOP:
        return square + " B ";
      case QUEEN:
        return square + " Q ";
      case KING:
        return square + " K ";
      case PAWN:
        return square + " P ";
    }
    return null;
  }
}
