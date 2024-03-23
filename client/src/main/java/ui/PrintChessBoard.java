package ui;

import static ui.EscapeSequences.*;

public class PrintChessBoard {
  public String printBothChessBoards() {
    return printWhiteChessBoard() + printBlackChessBoard();
  }

  public String printWhiteChessBoard() {
    String whiteChessBoard = "\n" +
            SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 8 " + SET_TEXT_COLOR_BLUE + SET_BG_COLOR_WHITE + " R " + SET_BG_COLOR_DARK_GREY + " N " + SET_BG_COLOR_WHITE + " B " + SET_BG_COLOR_DARK_GREY + " K " +
            SET_BG_COLOR_WHITE + " Q " + SET_BG_COLOR_DARK_GREY + " B " + SET_BG_COLOR_WHITE + " N " + SET_BG_COLOR_DARK_GREY + " R \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 8 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 7 " + SET_TEXT_COLOR_BLUE + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " +
            SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 7 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 6 " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " +
            SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 6 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 5 " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " +
            SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 5 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 4 " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " +
            SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 4 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 3 " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " +
            SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 3 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 2 " + SET_TEXT_COLOR_RED + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " +
            SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 2 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 1 " + SET_TEXT_COLOR_RED + SET_BG_COLOR_DARK_GREY + " R " + SET_BG_COLOR_WHITE + " N " + SET_BG_COLOR_DARK_GREY + " B " + SET_BG_COLOR_WHITE + " K " +
            SET_BG_COLOR_DARK_GREY + " Q " + SET_BG_COLOR_WHITE + " B " + SET_BG_COLOR_DARK_GREY + " N " + SET_BG_COLOR_WHITE + " R \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 1 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n\n"
            ;
    return whiteChessBoard;
  }

  public String printBlackChessBoard() {
    String blackChessBoard = "\n" +
            SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 8 " + SET_TEXT_COLOR_RED + SET_BG_COLOR_WHITE + " R " + SET_BG_COLOR_DARK_GREY + " N " + SET_BG_COLOR_WHITE + " B " + SET_BG_COLOR_DARK_GREY + " K " +
            SET_BG_COLOR_WHITE + " Q " + SET_BG_COLOR_DARK_GREY + " B " + SET_BG_COLOR_WHITE + " N " + SET_BG_COLOR_DARK_GREY + " R \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 8 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 7 " + SET_TEXT_COLOR_RED + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " +
            SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 7 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 6 " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " +
            SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 6 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 5 " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " +
            SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 5 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 4 " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " +
            SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 4 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 3 " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " +
            SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   " + SET_BG_COLOR_DARK_GREY + "   " + SET_BG_COLOR_WHITE + "   \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 3 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 2 " + SET_TEXT_COLOR_BLUE + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " +
            SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P " + SET_BG_COLOR_WHITE + " P " + SET_BG_COLOR_DARK_GREY + " P \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 2 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + " 1 " + SET_TEXT_COLOR_BLUE + SET_BG_COLOR_DARK_GREY + " R " + SET_BG_COLOR_WHITE + " N " + SET_BG_COLOR_DARK_GREY + " B " + SET_BG_COLOR_WHITE + " K " +
            SET_BG_COLOR_DARK_GREY + " Q " + SET_BG_COLOR_WHITE + " B " + SET_BG_COLOR_DARK_GREY + " N " + SET_BG_COLOR_WHITE + " R \u001B[0m" + SET_BG_COLOR_LIGHT_GREY  + " 1 \u001B[0m\n" +
            SET_BG_COLOR_LIGHT_GREY + "    a  b  c  d  e  f  g  h    \u001B[0m\n\n"
            ;
    return blackChessBoard;
  }

}
