package chess;
import java.util.ArrayList; // Added this line to return an empty array, you can remove.
import java.util.Arrays;
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
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING) {
            return calculateKingMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            return calculatePawnMoves(board, myPosition);
        }
        if (board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return calculateKnightMoves(board, myPosition);
        }
        return new ArrayList<>();
    }

    public ArrayList<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int[] rowAdvancements = {1, 1, -1, -1};
        int[] columnAdvancements = {1, -1, -1, 1};

        for (int i = 0; i < 4; i++) {
            ChessPosition currentPosition = startPosition;
            for (int j = 0; j < 6; j++) {
                ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + rowAdvancements[i], currentPosition.getColumn() + columnAdvancements[i]);
                if(endPosition.getColumn() < 9 && endPosition.getColumn() > 0 && endPosition.getRow() < 9 && endPosition.getRow() > 0) {
                    if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                        possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                    }
                }
                else {
                    break;
                }
                if (board.getPiece(endPosition) != null) {
                    break;
                }
                currentPosition = endPosition;
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int[] rowAdvancements = {0, 0, 1, -1};
        int[] columnAdvancements = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            ChessPosition currentPosition = startPosition;
            for (int j = 0; j < 6; j++) {
                ChessPosition endPosition = new ChessPosition(currentPosition.getRow() + rowAdvancements[i], currentPosition.getColumn() + columnAdvancements[i]);
                if(endPosition.getColumn() < 9 && endPosition.getColumn() > 0 && endPosition.getRow() < 9 && endPosition.getRow() > 0) {
                    if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                        possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                    }
                }
                else {
                    break;
                }
                if (board.getPiece(endPosition) != null) {
                    break;
                }
                currentPosition = endPosition;
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(calculateBishopMoves(board, startPosition));
        possibleMoves.addAll(calculateRookMoves(board, startPosition));

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateKingMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int[] rowAdvancements = {1, 0, -1, -1, -1, 0, 1, 1};
        int[] columnAdvancements = {1, 1, 1, 0, -1, -1, -1, 0};

        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + rowAdvancements[i], startPosition.getColumn() + columnAdvancements[i]);
            if(endPosition.getColumn() < 9 && endPosition.getColumn() > 0 && endPosition.getRow() < 9 && endPosition.getRow() > 0) {
                if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculateKnightMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int[] rowAdvancements = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnAdvancements = {1, 2, 2, 1, -1, -2, -2, -1};

        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + rowAdvancements[i], startPosition.getColumn() + columnAdvancements[i]);
            if(endPosition.getColumn() < 9 && endPosition.getColumn() > 0 && endPosition.getRow() < 9 && endPosition.getRow() > 0) {
                if (board.getPiece(endPosition) == null || board.getPiece(endPosition).pieceColor != board.getPiece(startPosition).pieceColor) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if (board.getPiece(startPosition).pieceColor == ChessGame.TeamColor.WHITE) {
            // Move one space until the end of the board is reached
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            if (board.getPiece(endPosition) == null && endPosition.getRow() < 9) {
                if (endPosition.getRow() == 8) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }

            // Move two spaces if row is 2
            if (startPosition.getRow() == 2) {
                endPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());

                if(board.getPiece(endPosition) == null && board.getPiece(new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }
            // If there is a piece to take, move on the diagonal
            ChessPosition rightDiagonal = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            ChessPosition leftDiagonal = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);

            if (board.getPiece(rightDiagonal) != null && board.getPiece(rightDiagonal).pieceColor != board.getPiece(startPosition).pieceColor) {
                if (rightDiagonal.getRow() == 8) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, null));
                }
            }

            if (board.getPiece(leftDiagonal) != null && board.getPiece(leftDiagonal).pieceColor != board.getPiece(startPosition).pieceColor) {
                if (leftDiagonal.getRow() == 8) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, null));
                }
            }
        }
        if (board.getPiece(startPosition).pieceColor == ChessGame.TeamColor.BLACK) {
            // Move one space until the end of the board is reached
            ChessPosition endPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            if (board.getPiece(endPosition) == null && endPosition.getRow() > 0) {
                if (endPosition.getRow() == 1) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }

            // Move two spaces if row is 2
            if (startPosition.getRow() == 7) {
                endPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());

                if(board.getPiece(endPosition) == null && board.getPiece(new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn())) == null) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }
            // If there is a piece to take, move on the diagonal
            ChessPosition rightDiagonal = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            ChessPosition leftDiagonal = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);

            if (board.getPiece(rightDiagonal) != null && board.getPiece(rightDiagonal).pieceColor != board.getPiece(startPosition).pieceColor) {
                if (rightDiagonal.getRow() == 1) {
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, rightDiagonal, null));
                }
            }

            if (board.getPiece(leftDiagonal) != null && board.getPiece(leftDiagonal).pieceColor != board.getPiece(startPosition).pieceColor) {
                if (leftDiagonal.getRow() == 1) {
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, PieceType.KNIGHT));
                }
                else {
                    possibleMoves.add(new ChessMove(startPosition, leftDiagonal, null));
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (type == PieceType.ROOK) {
                return "[R]";
            }
            if (type == PieceType.KNIGHT) {
                return "[N]";
            }
            if (type == PieceType.BISHOP) {
                return "[B]";
            }
            if (type == PieceType.QUEEN) {
                return "[Q]";
            }
            if (type == PieceType.KING) {
                return "[K]";
            }
            if (type == PieceType.PAWN) {
                return "[P]";
            }
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            if (type == PieceType.ROOK) {
                return "[r]";
            }
            if (type == PieceType.KNIGHT) {
                return "[n]";
            }
            if (type == PieceType.BISHOP) {
                return "[b]";
            }
            if (type == PieceType.QUEEN) {
                return "[q]";
            }
            if (type == PieceType.KING) {
                return "[k]";
            }
            if (type == PieceType.PAWN) {
                return "[p]";
            }
        }
        return null;
    }

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
