package aiproj.checkers.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created to have a unified place for modifying constants that affect how the game is played.
 */
public final class Config {
    
    public static final BufferedImage[] pieces = new BufferedImage[3]; // 0, red; 1, black; 2, king overlay
    
    public static final int PIECE_WIDTH = 70, PIECE_HEIGHT = 70;
    
    public static final int ROW_HEIGHT = 94, COLUMN_WIDTH = 94;
    
    public static final int X_OFFSET = 45, Y_OFFSET = 45; // x y offset in each cell
    
    public static final int WINDOW_WIDTH = 750, WINDOW_HEIGHT = 750;
    
    public static final int KING_WORTH = 5;
    
    public static final BufferedImage backgroundImage;
    
    public static final int WINNING_VALUE = 1000000;
    
    public static final int AI_DEPTH = 4;
    
    public static final boolean DEBUG = true;
    
    public static final int PIECE_WORTH = 1;
    
    static {
        BufferedImage backgroundImage1;
        try {
            backgroundImage1 = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/background.png"));
        } catch (IOException e) {
            backgroundImage1 = null;
        }
        backgroundImage = backgroundImage1;
    }
    
    
    private Config() {
    }
    
}
