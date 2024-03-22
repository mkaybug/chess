import chess.*;
import ui.PrintChessBoard;
import ui.Repl;
import ui.PrintChessBoard;

public class Main {
    public static void main(String[] args) {
        PrintChessBoard printBoard = new PrintChessBoard();
        printBoard.printBlackChessBoard();

//        var serverUrl = "http://localhost:8080";
//        if (args.length == 1) {
//            serverUrl = args[0];
//        }
//        new Repl(serverUrl).run();
    }
}