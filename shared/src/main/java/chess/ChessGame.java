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
    private ChessGame.TeamColor teamTurn = TeamColor.WHITE;

    public ChessGame() {}

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
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        // Funneling pieceMoves() for actually valid moves
        if (piece != null) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(chessBoard, startPosition);

            for (ChessMove move : possibleMoves) {
                ChessBoard testBoard = new ChessBoard();
                // Clone chessBoard and test out each move -> using deep copy
                for (int i = 1; i < 9; i++) {
                    for (int j = 1; j < 9; j++) {
                        testBoard.addPiece(new ChessPosition(i, j), chessBoard.getPiece(new ChessPosition(i, j)));
                    }
                }

                // Making the test move, if the piece is a pawn and has reached the other side of the board, promote
                if(piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    testBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                }
                else {
                    testBoard.addPiece(move.getEndPosition(), piece);
                }
                testBoard.removePiece(startPosition);

                // Test to see if this move puts our king in check, if not, add to validMoves
                ChessPosition kingPosition = findKing(testBoard, piece.getTeamColor());

                if(!calculateIsInCheck(testBoard, kingPosition)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece piece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());

        // Invalid move cases
        if (possibleMoves == null) {
            throw new InvalidMoveException();
        }

        if (!possibleMoves.contains(move)) {
            throw new InvalidMoveException();
        }

        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }

        // Move the piece, if it is a pawn and has reached the other side of the board, promote it
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            chessBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            chessBoard.addPiece(move.getEndPosition(), piece);
        }
        chessBoard.removePiece(startPosition);

        // Move on to the other player's turn
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        }
        else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(chessBoard, teamColor);
        return calculateIsInCheck(chessBoard, kingPosition);
    }

    /**
     * Assists validMoves() isInCheck() and isInCheckmate() in calculations
     *
     * @param board the board for which calculations are performed (member variable or temporary board)
     * @param kingPosition the current location of the king
     * @return True if the specified team is in check
     */
    public boolean calculateIsInCheck(ChessBoard board, ChessPosition kingPosition) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(currentPosition);
                if (piece != null) {
                    for (ChessMove move : piece.pieceMoves(board, currentPosition)) {
                        if(move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Finds the king piece for a given team
     *
     * @param board the board on which to find the king (member variable or temporary board)
     * @param teamColor which team to find the king for
     * @return The king's current position
     */
    public ChessPosition findKing(ChessBoard board, TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == teamColor && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        return currentPosition;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // If teamColor is in check and has no legal move, checkmate = true
        // Check all moves for the current team.
        // If one of the pieces has a valid move, then return false.
        if (isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition currentPosition = new ChessPosition(i, j);
                    ChessPiece piece = chessBoard.getPiece(currentPosition);

                    if(piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validMoves = validMoves(currentPosition);
                        if(!validMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // If teamColor is not in check and has no legal move, stalemate = true
        if (!isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition currentPosition = new ChessPosition(i, j);
                    ChessPiece piece = chessBoard.getPiece(currentPosition);

                    if(piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validMoves = validMoves(currentPosition);
                        if(!validMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        else {
            return false;
        }
        return true;
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
