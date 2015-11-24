package aiproj.checkers.graphics;

import aiproj.checkers.game.CheckersBoard;
import mknutsen.graphicslibrary.GraphicsComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mknutsen on 10/28/15.
 */
public class EndScreen extends GraphicsComponent implements MouseListener {

    private int winner;

    private CheckersBoard board;

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.setFont(new Font("Ariel", Font.BOLD, 60));
        g.setColor(Color.red);
        g.drawImage(Config.backgroundImage, 0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, null);
        board.draw(g);
        System.out.println("game text: "+board.getBoardString());
        g.drawString("Winner was: " + winner, 100, 100);
    }

    @Override
    public void takeParameters(final Object[] obj) {
        winner = (int) obj[0];
        board = (CheckersBoard) obj[1];
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {
        triggerCallback();
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
