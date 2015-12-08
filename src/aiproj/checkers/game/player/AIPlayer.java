package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.game.player.ai.GameTree;
import aiproj.checkers.graphics.Config;
import aiproj.checkers.graphics.StartScreen;

import java.util.List;

/**
 * Player that uses the GameTree to figure out what move to make.
 */
public class AIPlayer extends Player {

    private GameTree tree;

    private StartScreen.Heuristic heuristic;

    /**
     * No heuristic demanded, just uses whichever the game has set to use.
     */
    public AIPlayer() {
        this(null);
    }

    /**
     * @param heuristic
     *         heuristic to use for this AI
     */
    public AIPlayer(StartScreen.Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    protected void processGame() {
        tree = new GameTree(getGame(), heuristic);
    }

    @Override
    public CheckersBoard.Move makeTurn(final List<CheckersBoard.Move> availableMoves) {
        while (tree.isThinking()) {
        }
        if (Config.DEBUG) {
            System.out.println("time to start the AI");
        }

        //turns the board string into only the last move
        CheckersBoard board = getGame();
        String gameString = board.getBoardString();
        if (gameString != null && gameString.indexOf("\n") > 0) {
            while (gameString.indexOf("\n") != gameString.lastIndexOf("\n")) {
                gameString = gameString.substring(gameString.indexOf("\n") + 1);
            }
            tree.intakeLastMove(gameString);
        }
        return tree.bestMove();
    }

    /**
     * @return true if the gametree is in a thread trying to figure out what to do
     */
    public boolean isThinking() {
        return tree.isThinking();
    }

}
