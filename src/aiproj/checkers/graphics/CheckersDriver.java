package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.GraphicsDriver;

/**
 * Created by mknutsen on 10/21/15.
 */
public class CheckersDriver {

    public static void main(String[] args) {
        new GraphicsDriver(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, new GameComponent());
    }
}
