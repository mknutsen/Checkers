package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;
import aiproj.checkers.graphics.GameComponent;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Player gives access to the components necessary to play without giving player the ability to mess with the game.
 */
public abstract class Player {

    private CheckersBoard game;

    private boolean player1;

    private CheckersPiece hover = null;

    private GameComponent.RepaintCallback repaintCallback;

    /**
     * @param board
     *         board to get pieces from
     * @param player1
     *         player 1 or player 2
     * @return that players pieces
     */
    public static ArrayList<CheckersPiece> getMyPieces(CheckersBoard board, boolean player1) {
        if (player1) {
            return board.getPlayer1Pieces();
        } else {
            return board.getPlayer2Pieces();
        }
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public final String toString() {
        return super.toString();
    }

    /**
     * @return the moves the player can make
     */
    public final Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> getAvailableMoves() {
        Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> movesTable = new Hashtable<>();
        CheckersBoard game = getGame();
        ArrayList<CheckersPiece> myPieces = getMyPieces(game, player1);
        if (!myPieces.isEmpty()) {
            for (CheckersPiece piece : myPieces) {
                ArrayList<CheckersBoard.Coordinate> moves = new ArrayList<>();
                CheckersBoard.MovesList moveList =
                        CheckersBoard.getMoves(piece.getRow(), piece.getCol(), player1, piece.getIsKing());
                game.removeImpossibleMoves(moveList).forEach(move -> moves.add(move));
                game.getOneJumps(piece, moveList).forEach(move -> moves.add(move));
                if (!moves.isEmpty()) {
                    movesTable.put(piece, moves);
                }
            }
        }
        return movesTable;

    }

    /**
     * @return a cloned copy of the game. I repeat: a cloned copy.
     */
    public final CheckersBoard getGame() {

        return (CheckersBoard) game.clone();
    }

    /**
     * @param game
     *         the game the player is engaged in
     * @param player1
     *         is it player1?
     */
    public final void setGame(CheckersBoard game, boolean player1) {
        this.game = game;
        this.player1 = player1;
    }

    /**
     * gets called when its your turn
     *
     * @return the move you want to make or null if you dont want to make a move right now. if you return null, just
     * call player.makeMove when you're ready.
     */
    public final CheckersBoard.Move triggerTurn() {
        Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> availableMoves = getAvailableMoves();
        if (availableMoves.isEmpty()) {
            game.declareWinner(!getIsPlayer1());
            return null;
        } else {
            return makeTurn(availableMoves);
        }
    }


    /**
     * make a move that will affect the real board
     *
     * @param move
     *         describes the move to be made
     * @return true if sucessful
     */
    public final boolean makeMove(CheckersBoard.Move move) {
        return makeMove(move.getPiece(), move.getEndCell());

    }

    /**
     * make a move that will affect the real board
     *
     * @param piece
     *         piece to move
     * @param endCell
     *         place to move it to
     * @return true if sucessful
     */
    public boolean makeMove(final CheckersPiece piece, final CheckersBoard.Coordinate endCell) {
        System.out.println("move: " + piece + " to " + endCell);
        boolean move = game.makeMove(game.getPiece(piece.getRow(), piece.getCol()), endCell);
        repaintCallback.repaint();
        return move;
    }

    /**
     * @param repaintCallback
     *         callback to let the player call repaint on the board
     */
    public void setRepaintCallback(final GameComponent.RepaintCallback repaintCallback) {
        this.repaintCallback = repaintCallback;
    }

    /**
     * @param coordinate
     *         sets the location of the piece thats being dragged
     */
    public final void setHover(CheckersBoard.Coordinate coordinate) {
        if (hover != null) {
            if (coordinate.getCol() == -1) {
                hover.setHover(-1, -1);
                hover = null;
            } else {
                hover.setHover(coordinate.getRow(), coordinate.getCol());
            }
            repaintCallback.repaint();

        }
    }

    /**
     * @return a clone of the piece thats being hovered on
     */
    public final CheckersPiece getHover() {
        return hover == null ? null : (CheckersPiece) hover.clone();
    }

    /**
     * @param piece
     *         piece to be hovering
     */
    public final void setHover(CheckersPiece piece) {
        if (piece != null && piece.getIsPlayer1() == getIsPlayer1()) {
            hover = game.getPiece(piece.getRow(), piece.getCol());
        }
    }

    public final boolean getIsPlayer1() {
        return player1;
    }

    /**
     * Make a move
     *
     * @param availableMoves
     *         the moves you can make in a hashtable <piece, list of moves>
     * @return the move to make, or null. if you return null, call makeMove when you have a move to make.
     */
    public abstract CheckersBoard.Move makeTurn(
            final Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> availableMoves);
}
