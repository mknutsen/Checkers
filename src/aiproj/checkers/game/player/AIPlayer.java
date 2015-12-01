package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.game.player.ai.GameTree;
import aiproj.checkers.graphics.Config;

import java.util.List;

/**
 * Created by mknutsen on 11/24/15.
 */
public class AIPlayer extends Player {

    private GameTree tree;

    public AIPlayer() {
    }

    @Override
    protected void processGame() {
        tree = new GameTree(getGame());
    }

    @Override
    public CheckersBoard.Move makeTurn(final List<CheckersBoard.Move> availableMoves) {
        while (tree.isThinking()) {
        }
        if (Config.DEBUG) {
            System.out.println("time to start the AI");
        }
        CheckersBoard board = getGame();
        String gameString = board.getBoardString();
        if (gameString != null && gameString.indexOf("\n") > 0) {
            while (gameString.indexOf("\n") != gameString.lastIndexOf("\n")) {
                if (Config.DEBUG) {
                    System.out.println("Game string: " + gameString);
                }
                gameString = gameString.substring(gameString.indexOf("\n") + 1);
            }
            tree.intakeLastMove(gameString);
        }
        return tree.bestMove();
    }
}
