package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.GraphicsComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mknutsen on 10/28/15.
 */
public class EndScreen extends GraphicsComponent implements MouseListener {

    private int winner;

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.drawString("Winner was: " + winner, 100, 100);
    }

    @Override
    public void takeParameters(final Object[] obj) {
        winner = (int) obj[0];
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
