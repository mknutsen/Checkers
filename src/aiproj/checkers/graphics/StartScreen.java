package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.GraphicsComponent;
import mknutsen.graphicslibrary.GraphicsHelperFunctions;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by mknutsen on 12/1/15.
 */
public class StartScreen extends GraphicsComponent implements MouseListener {
    
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

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        super.paint(g);
        g.setFont(new Font("Ariel", Font.BOLD, 60));
        g.setColor(Color.red);
        GraphicsHelperFunctions.drawCenteredString(g, "Welcome to\nCheckers!", 100, Config.WINDOW_WIDTH);
    }
}
