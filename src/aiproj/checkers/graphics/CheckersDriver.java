package aiproj.checkers.graphics;

import aiproj.checkers.game.player.MousePlayer;
import aiproj.checkers.game.player.RandomPlayer;
import mknutsen.graphicslibrary.GraphicsDriver;

/**
 * creates a graphics driver that runs the game component.
 */
public class CheckersDriver {

    public static void main(String[] args) {
        new GraphicsDriver(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT,
                new GameComponent(new MousePlayer(), new RandomPlayer()), new EndScreen());
    }
}
