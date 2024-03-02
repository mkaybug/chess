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

    public ArrayList<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition, int[] rowAdvancements, int[] columnAdvancements) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

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

    /**
     * Calculates all possible bishop moves
     *
     * @param board The chessboard
     * @param startPosition The starting position of the bishop to be moved
     * @return ArrayList of valid moves
     */
    public ArrayList<ChessMove> calculateBishopMoves(ChessBoard board, ChessPosition startPosition) {
        int[] rowAdvancements = {1, 1, -1, -1};
        int[] columnAdvancements = {1, -1, -1, 1};

        return calculateMoves(board, startPosition, rowAdvancements, columnAdvancements);
    }

    /**
     * Calculates all possible rook moves
     * Near identical functionality to calculateBishopMoves()
     *
     * @param board The chessboard
     * @param startPosition The starting position of the rook to be moved
     * @return ArrayList of valid moves
     */
    public ArrayList<ChessMove> calculateRookMoves(ChessBoard board, ChessPosition startPosition) {
        int[] rowAdvancements = {0, 0, 1, -1};
        int[] columnAdvancements = {1, -1, 0, 0};

        return calculateMoves(board, startPosition, rowAdvancements, columnAdvancements);
    }

    /**
     * Calculates all possible queen moves
     * Implements functionality of calculateBishopMoves() and calculateRookMoves()
     *
     * @param board The chessboard
     * @param startPosition The starting position of the queen to be moved
     * @return ArrayList of valid moves
     */
    public ArrayList<ChessMove> calculateQueenMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(calculateBishopMoves(board, startPosition));
        possibleMoves.addAll(calculateRookMoves(board, startPosition));

        return possibleMoves;
    }

    /**
     * Calculates all possible king moves
     *
     * @param board The chessboard
     * @param startPosition The starting position of the king to be moved
     * @return ArrayList of valid moves
     */
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

    /**
     * Calculates all possible knight moves
     * Near identical functionality to calculateKingMoves()
     *
     * @param board The chessboard
     * @param startPosition The starting position of the knight to be moved
     * @return ArrayList of valid moves
     */
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

    /**
     * Calculates all possible pawn moves
     * Near identical functionality to calculatePawnMoves()
     *
     * @param board The chessboard
     * @param startPosition The starting position of the pawn to be moved
     * @return ArrayList of valid moves
     */
    public ArrayList<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> endPositions = new ArrayList<>();

        if (board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Checking for possible forward moves
            ChessPosition oneForward = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            if (board.getPiece(oneForward) == null) {
                endPositions.add(oneForward);
                if (startPosition.getRow() == 2) {
                    ChessPosition twoForward = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
                    if (board.getPiece(twoForward) == null) {
                        endPositions.add(twoForward);
                    }
                }
            }

            // Checking for possible captures (diagonal moves)
            if (startPosition.getColumn() > 1) {
                ChessPosition leftDiagonal = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
                if(board.getPiece(leftDiagonal) != null && board.getPiece(leftDiagonal).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    endPositions.add(leftDiagonal);
                }
            }

            if (startPosition.getColumn() < 8) {
                ChessPosition rightDiagonal = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
                if(board.getPiece(rightDiagonal) != null && board.getPiece(rightDiagonal).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    endPositions.add(rightDiagonal);
                }
            }
        }
        else if (board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            // Checking for possible forward moves
            ChessPosition oneForward = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            if (board.getPiece(oneForward) == null) {
                endPositions.add(oneForward);
                if (startPosition.getRow() == 7) {
                    ChessPosition twoForward = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
                    if (board.getPiece(twoForward) == null) {
                        endPositions.add(twoForward);
                    }
                }
            }

            // Checking for possible captures (diagonal moves)
            if (startPosition.getColumn() < 8) {
                ChessPosition leftDiagonal = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
                if(board.getPiece(leftDiagonal) != null && board.getPiece(leftDiagonal).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    endPositions.add(leftDiagonal);
                }
            }
            if (startPosition.getColumn() > 1) {
                ChessPosition rightDiagonal = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
                if(board.getPiece(rightDiagonal) != null && board.getPiece(rightDiagonal).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    endPositions.add(rightDiagonal);
                }
            }
        }

        // for loop to iterate through, check for boundaries, and promotions

        for (ChessPosition endPosition : endPositions) {
            if (endPosition.getColumn() < 9 && endPosition.getColumn() > 0) {
                if (endPosition.getRow() < 8 && endPosition.getRow() > 1) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, null));
                }
                else if (endPosition.getRow() == 8 || endPosition.getRow() == 1) {
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
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
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            if(type == PieceType.ROOK) {
                return "[r]";
            }
            if(type == PieceType.KNIGHT) {
                return "[n]";
            }
            if(type == PieceType.BISHOP) {
                return "[b]";
            }
            if(type == PieceType.QUEEN) {
                return "[q]";
            }
            if(type == PieceType.KING) {
                return "[k]";
            }
            if(type == PieceType.PAWN) {
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
