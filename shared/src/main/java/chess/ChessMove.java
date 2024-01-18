package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Returns a string representation of the ChessMove.
     *
     * @return A string representation of the ChessMove.
     */
    @Override
    public String toString() {
        String [] myArray = {"Hello", "World"};
        return myArray[0];

//        return "Possible moves: (" + getEndPosition().getRow() + ", " +
//                getEndPosition().getColumn() + ")"
//                "Row: " + getStartPosition().getRow() +
//                ", endPosition=" + getEndPosition() +
//                ", promotionPiece=" + getPromotionPiece() +
//                '}';
    }
}
