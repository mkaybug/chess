import chess.*;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MySQLDAOs.MySQLUserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);

        MySQLUserDAO userDataAccess = new MySQLUserDAO();
    }
}