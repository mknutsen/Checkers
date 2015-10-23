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
 * Created by mknutsen on 10/21/15.
 */
public class GameComponent extends GraphicsComponent implements MouseListener {

    public static BufferedImage background;

    static {
        try {
            background = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    CheckersBoard board;

    public GameComponent() {
        board = new CheckersBoard();
        board.newGame();
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
