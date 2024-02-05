package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessBoard;
    private ChessGame.TeamColor teamTurn;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        return piece.pieceMoves(chessBoard, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        if (startPosition.getRow() < 1 || startPosition.getRow() > 8 || startPosition.getColumn() < 1 || startPosition.getColumn() > 8) {
            throw new InvalidMoveException(("Out of bounds move"));
        }

        ChessPiece piece = chessBoard.getPiece(startPosition);
        System.out.println(piece.toString());

        Collection<ChessMove> validMoves = piece.pieceMoves(chessBoard, startPosition);
//        if (validMoves == null || !validMoves.contains(move) ) {
//            System.out.println("Throwing exception");
//            throw new InvalidMoveException("Invalid move");
//        }

        chessBoard.addPiece(move.getEndPosition(), piece);
        chessBoard.removePiece(startPosition);
        System.out.println(chessBoard.toString());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;
//        Collection<ChessMove> opposingTeamMoves = null;
//        ChessPosition kingPosition = null;
//
//
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                ChessPosition currentPosition = new ChessPosition(i, j);
//                ChessPiece currentPiece = chessBoard.getPiece(currentPosition);
//                if (currentPiece != null) {
//                    if (currentPiece.getTeamColor() == teamColor && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
//                        kingPosition = currentPosition;
//                    }
//                    if (currentPiece.getTeamColor() != teamColor) {
//                        opposingTeamMoves.addAll(currentPiece.pieceMoves(chessBoard, currentPosition));
//                    }
//                }
//            }
//        }
//
//        if (opposingTeamMoves != null && kingPosition != null) {
//            for (ChessMove move : opposingTeamMoves) {
//                if (move.getEndPosition() == kingPosition) {
//                    check = true;
//                }
//            }
//        }

        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkMate = false;
        return checkMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = false;
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
