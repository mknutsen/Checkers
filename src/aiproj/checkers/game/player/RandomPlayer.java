package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * implementation of player that makes a move randomly when triggered.
 */
public final class RandomPlayer extends Player {
    
    @Override
    public CheckersBoard.Move makeTurn(final Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> movesTable) {
        //decides which of the pieces will be moved
        Object[] piecesToMove = movesTable.keySet().toArray();
        CheckersPiece pieceToMove = (CheckersPiece) piecesToMove[((int) (piecesToMove.length * Math.random()))];
        //decides which of the moves available to the piece will be taken
        ArrayList<CheckersBoard.Coordinate> movePossibilities = movesTable.get(pieceToMove);
        CheckersBoard.Coordinate move = movePossibilities.get((int) (movePossibilities.size() * Math.random()));
        //submits the move
        return new CheckersBoard.Move(pieceToMove, move);
    }
}
