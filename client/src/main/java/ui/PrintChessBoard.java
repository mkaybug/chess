package ui;

import chess.*;

import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class PrintChessBoard {
  ChessGame chessGame;
  ChessGame.TeamColor teamColor;

  public PrintChessBoard(ChessGame chessGame, ChessGame.TeamColor teamColor) {
    this.chessGame = chessGame;
    this.teamColor = teamColor;
  }

  public String printBoard() {
    if (Objects.equals(teamColor, ChessGame.TeamColor.WHITE)) {
      return printWhiteTeamBoard();
    }
    else if (Objects.equals(teamColor , ChessGame.TeamColor.BLACK)) {
      return printBlackTeamBoard();
    }
    else {
      return printWhiteTeamBoard();
    }
  }

  public String highlightPossibleMoves(int row, int column) {
    if (Objects.equals(teamColor, ChessGame.TeamColor.WHITE)) {
      return whiteHighlightMoves(row, column);
    }
    else if (Objects.equals(teamColor , ChessGame.TeamColor.BLACK)) {
      return blackHighlightMoves(row, column);
    }
    else {
      return whiteHighlightMoves(row, column);
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
    board.append(SET_TEXT_COLOR_BLUE + "Type help for possible actions.");
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
    board.append(SET_TEXT_COLOR_BLUE + "Type help for possible actions.");
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

    ChessPiece piece = chessGame.getBoard().getPiece(new ChessPosition(i, j));

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

  public String whiteHighlightMoves(int row, int column) {
    ChessPosition startPosition = new ChessPosition(row, column);
    Collection<ChessMove> possibleMoves = chessGame.validMoves(startPosition);

    StringBuilder board = new StringBuilder();
    board.append("\n\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n");

    for (int i = 8; i > 0; i--) {
      boolean whiteSpace = i % 2 == 0;
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" ");

      for (int j = 8; j > 0; j--) {
        if (possibleMoves.contains(new ChessMove(startPosition, new ChessPosition(i, j), null))) {
          whiteSpace = createHighlightSquare(board, i, whiteSpace, j);
        }
        else {
          whiteSpace = createSquareString(board, i, whiteSpace, j);
        }
      }

      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" \u001B[0m\n");
    }

    board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n\n");
    board.append(SET_TEXT_COLOR_BLUE + "Type help for possible actions.");
    return String.valueOf(board);
  }

  public String blackHighlightMoves(int row, int column) {
    ChessPosition startPosition = new ChessPosition(row, column);
    Collection<ChessMove> possibleMoves = chessGame.validMoves(startPosition);

    StringBuilder board = new StringBuilder();
    board.append("\n\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    \u001B[0m\n");

    for (int i = 1; i < 9; i++) {
      boolean whiteSpace = i % 2 != 0;
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" ");

      for (int j = 1; j < 9; j++) {
        if (possibleMoves.contains(new ChessMove(startPosition, new ChessPosition(i, j), null))) {
          whiteSpace = createHighlightSquare(board, i, whiteSpace, j);
        }
        else {
          whiteSpace = createSquareString(board, i, whiteSpace, j);
        }
      }
      board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + " ").append(i).append(" \u001B[0m\n");
    }

    board.append("\u001B[0m" + SET_BG_COLOR_LIGHT_GREY + "    h  g  f  e  d  c  b  a    \u001B[0m\n\n");
    board.append(SET_TEXT_COLOR_BLUE + "Type help for possible actions.");
    return String.valueOf(board);
  }

  private boolean createHighlightSquare(StringBuilder board, int i, boolean whiteSpace, int j) {
    whiteSpace = !whiteSpace;

    board.append(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_DARK_GREY);
    ChessPiece piece = chessGame.getBoard().getPiece(new ChessPosition(i, j));

    if (piece != null) {
      board.append(createPieceString(piece));
    }
    else {
      board.append("   ");
    }
    return whiteSpace;
  }
}
