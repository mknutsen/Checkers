package aiproj.checkers.graphics;

import aiproj.checkers.game.player.AIPlayer;
import aiproj.checkers.game.player.MousePlayer;
import mknutsen.graphicslibrary.GraphicsDriver;

/**
 * creates a graphics driver that runs the game component.
 */
public class CheckersDriver {

    public static void main(String[] args) {
        new GraphicsDriver(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, new StartScreen(),
                new GameComponent(new MousePlayer(), new AIPlayer()), new EndScreen());
    }
}
