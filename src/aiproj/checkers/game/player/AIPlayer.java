package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.game.player.ai.GameTree;
import aiproj.checkers.graphics.CheckersPiece;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by mknutsen on 11/24/15.
 */
public class AIPlayer extends Player {

    private GameTree tree;

    public AIPlayer() {
        tree = new GameTree(getGame());
    }

    @Override
    public CheckersBoard.Move makeTurn(
            final Hashtable<CheckersPiece, ArrayList<CheckersBoard.Coordinate>> availableMoves) {
        CheckersBoard board = getGame();
        String gameString = board.getBoardString();
        if (gameString != null && gameString.indexOf("\n") > 0) {
            while (gameString.indexOf("\n") != gameString.lastIndexOf("\n")) {
                gameString = gameString.substring(gameString.indexOf("\n") + 2);
            }
            tree.intakeLastMove(gameString);
        }
        return tree.bestMove();
    }
}
