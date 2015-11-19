package aiproj.checkers.graphics;

import java.awt.image.BufferedImage;

/**
 * Created to have a unified place for modifying constants that affect how the game is played.
 */
public final class Config {

    public static final BufferedImage[] pieces = new BufferedImage[3]; // 0, red; 1, black; 2, king

    public static final int PIECE_WIDTH = 70, PIECE_HEIGHT = 70;

    public static final int ROW_HEIGHT = 94, COLUMN_WIDTH = 94;

    public static final int X_OFFSET = 45, Y_OFFSET = 45; // x y offset in each cell

    public static final int WINDOW_WIDTH = 750, WINDOW_HEIGHT = 750;

    public static final int KING_WORTH = 1;

    private Config() {

    }

}
