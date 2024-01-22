package chess;
import java.util.ArrayList; // Added this line to return an empty array, you can remove.
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP) {
            return calculateBishopMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    public ArrayList<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition currentPosition = startPosition;

        while(currentPosition.getRow() < 8 && currentPosition.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() + 1);
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            currentPosition = endPosition;

            if(board.getPiece(currentPosition) != null) {
                break;
            }
        }

        currentPosition = startPosition;

        while(currentPosition.getRow() < 8 && currentPosition.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() - 1);
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            currentPosition = endPosition;

            if(board.getPiece(currentPosition) != null) {
                break;
            }
        }

        currentPosition = startPosition;

        while(currentPosition.getRow() > 1 && currentPosition.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() + 1);
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            currentPosition = endPosition;

            if(board.getPiece(currentPosition) != null) {
                break;
            }
        }

        currentPosition = startPosition;

        while(currentPosition.getRow() > 1 && currentPosition.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 1);
            possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            currentPosition = endPosition;

            if(board.getPiece(currentPosition) != null) {
                break;
            }
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "(" + pieceColor + " " + type + ')';
    }

    // FIXME I generated these but only ended up needing the hashCode() override in the ChessMove class, delete them if they cause problems
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
