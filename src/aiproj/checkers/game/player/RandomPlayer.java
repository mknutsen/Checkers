package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;

import java.util.List;

/**
 * implementation of player that makes a move randomly when triggered.
 */
public final class RandomPlayer extends Player {
    
    @Override
    protected void processGame() {
        //no need to process the game
    }

    @Override
    public CheckersBoard.Move makeTurn(final List<CheckersBoard.Move> movesList) {
        return movesList.get((int) (movesList.size() * Math.random()));
    }
}
