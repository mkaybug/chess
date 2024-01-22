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
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            return calculateRookMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
            return calculateQueenMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
//            return calculateKnightMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING) {
//            return calculateKingMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    public ArrayList<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition currentPosition = startPosition;

        while (currentPosition.getRow() < 8 && currentPosition.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getRow() < 8 && currentPosition.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getRow() > 1 && currentPosition.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getRow() > 1 && currentPosition.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition currentPosition = startPosition;

        while (currentPosition.getRow() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn());
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getRow() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn());
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getColumn() < 8) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        currentPosition = startPosition;

        while (currentPosition.getColumn() > 1) {
            ChessPosition endPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                possibleMoves.add(new ChessMove(startPosition, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
            currentPosition = endPosition;
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(calculateBishopMoves(board, startPosition));
        possibleMoves.addAll(calculateRookMoves(board, startPosition));

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
