package aiproj.checkers.graphics;

import aiproj.checkers.game.CheckersBoard;
import mknutsen.graphicslibrary.GraphicsComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * game component handles interfacing the board and the players as well as displaying the graphics
 */
public class GameComponent extends GraphicsComponent implements MouseListener {

    public static BufferedImage background;

    /**
     * Handles loading the background image
     */
    static {
        try {
            background = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CheckersBoard board;

    /**
     * Default constructor initializes the board and the players and the mouse listener.
     */
    public GameComponent() {
        board = new CheckersBoard();
        board.newGame();
        addMouseListener(this);
    }

    @Override
    public void paint(final Graphics g) {
        g.drawImage(background, 0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, null);
        board.draw(g);
    }

    @Override
    public void takeParameters(final Object[] obj) {

    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}
