package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;
import aiproj.checkers.graphics.GameComponent;

import java.util.ArrayList;

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
    public final ArrayList<CheckersBoard.Coordinate> getAvailableMoves() {
        CheckersBoard game = getGame();
        for (CheckersPiece piece : getMyPieces(game, player1)) {
            game.removeImpossibleMoves(CheckersBoard.getMoves(piece.getRow(), piece.getCol(), player1));

        }
        return null;

    }

    /**
     * @return a cloned copy of the game. I repeat: a cloned copy.
     */
    public final CheckersBoard getGame() {
        try {
            return (CheckersBoard) game.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
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
     * @return when you complete a turn, call trigger move. this is important.
     */
    public abstract CheckersBoard.Move triggerTurn();


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
}
