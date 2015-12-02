package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.GraphicsComponent;
import mknutsen.graphicslibrary.GraphicsHelperFunctions;
import mknutsen.graphicslibrary.graphicsobject.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mknutsen on 12/1/15.
 */
public class StartScreen extends GraphicsComponent implements MouseListener {

    private static final BufferedImage debugOn, debugOff, backgroundImage, done;

    static {
        BufferedImage debugOn1 = null, debugOff1 = null, backgroundImage1 = null, done1 = null;
        try {
            debugOn1 = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/debugOn.png"));
            debugOff1 = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/debugOff.png"));
            backgroundImage1 = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/menuWelcome.png"));
            done1 = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/done.png"));
        } catch (IOException e) {

        } finally {
            debugOn = debugOn1;
            debugOff = debugOff1;
            backgroundImage = backgroundImage1;
            done = done1;
        }

    }

    private ArrayList<Button> buttons;

    public StartScreen() {
        buttons = new ArrayList<>();
        buttons.add(new DebugButton(80, 400, 200, 100));
        buttons.add(new Button(470, 400, 200, 100, done, new Button.ButtonCallback() {

            @Override
            public void trigger() {
                triggerCallback();
            }
        }));
        addMouseListener(this);
    }

    @Override
    public void takeParameters(final Object[] obj) {

    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        buttons.forEach(button -> {
            if (button.isInside(e)) {
                button.click();
            }
        });
        repaint();

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
        g.drawImage(backgroundImage, 0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, null);
        g.setFont(new Font("Ariel", Font.BOLD, 60));
        g.setColor(new Color(128, 128, 128));
        GraphicsHelperFunctions.drawCenteredString(g, "Welcome to\nCheckers!", 100, Config.WINDOW_WIDTH);
        buttons.forEach(button -> button.draw(g));
    }

    private class DebugButton extends Button {


        public DebugButton(final int x, final int y, final int width, final int height) {
            super(x, y, width, height, "", Config.DEBUG ? debugOn : debugOff);
        }

        @Override
        public boolean isInside(final int x, final int y) {
            boolean isInside = super.isInside(x, y);
            if (isInside) {
                if (getImage() == debugOn) {
                    setImage(debugOff);
                } else {
                    setImage(debugOn);
                }
                Config.DEBUG = !Config.DEBUG;
            }
            return isInside;
        }
    }
}
