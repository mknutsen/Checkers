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
 * Screen to show to choose heuristic and turn debug on/off
 */
public class StartScreen extends GraphicsComponent implements MouseListener {

    private static final BufferedImage debugOn, debugOff, backgroundImage, done;

    private static BufferedImage[] a, b, c;

    /**
     * load a bunch of pictures.
     */
    static {
        BufferedImage debugOn1 = null, debugOff1 = null, backgroundImage1 = null, done1 = null;
        a = new BufferedImage[2];
        b = new BufferedImage[2];
        c = new BufferedImage[2];
        try {
            a[0] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/aOn.png"));
            a[1] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/aOff.png"));

            b[0] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/bOn.png"));
            b[1] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/bOff.png"));

            c[0] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/cOn.png"));
            c[1] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/cOff.png"));

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

    private Heuristic heuristic;

    private ArrayList<Button> buttons;

    private ArrayList<ToggleButton> toggleButtons;

    /**
     * sets up the buttons.
     */
    public StartScreen() {
        heuristic = Heuristic.A;

        buttons = new ArrayList<>();
        toggleButtons = new ArrayList<>();
        final int heuristicY = 450;
        final int doneDebugY = 300;
        buttons.add(new ToggleButton(80, doneDebugY, 200, 100, debugOn, debugOff, Config.DEBUG,
                new Button.ButtonCallback() {

                    @Override
                    public void trigger() {
                        Config.DEBUG = !Config.DEBUG;
                    }
                }));
        buttons.add(new Button(470, doneDebugY, 200, 100, done, new Button.ButtonCallback() {

            @Override
            public void trigger() {
                System.out.println(heuristic);
                triggerCallback(heuristic);
            }
        }));

        toggleButtons.add(new ToggleButton(50, heuristicY, 200, 100, a[0], a[1], true, new Button.ButtonCallback() {

            @Override
            public void trigger() {
                heuristic = Heuristic.A;
            }
        }));
        toggleButtons.add(new ToggleButton(Config.WINDOW_WIDTH / 2 - 100, heuristicY, 200, 100, b[0], b[1], false,
                new Button.ButtonCallback() {

                    @Override
                    public void trigger() {
                        heuristic = Heuristic.B;

                    }
                }));
        toggleButtons.add(new ToggleButton(500, heuristicY, 200, 100, c[0], c[1], false, new Button.ButtonCallback() {

            @Override
            public void trigger() {
                heuristic = Heuristic.C;

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
        toggleButtons.forEach(button -> {
            if (button.isInside(e)) {
                toggleButtons.forEach(button2 -> button2.unclick());
                button.isInside(e);
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
        toggleButtons.forEach(radioButton -> radioButton.draw(g));
    }

    public static enum Heuristic {
        A("A"), B("B"), C("C");

        private final String letter;

        Heuristic(String letter) {
            this.letter = letter;
        }

        public String toString() {
            return letter;
        }
    }

    /**
     * toggle button changes the image to show it being clicked or not clicked.
     */
    private class ToggleButton extends Button {


        private final BufferedImage onImage;

        private BufferedImage offImage;

        public ToggleButton(final int x, final int y, final int width, final int height, BufferedImage onImage,
                            BufferedImage offImage, boolean startingValue, ButtonCallback callback) {
            super(x, y, width, height, startingValue ? onImage : offImage, callback);
            this.offImage = offImage;
            this.onImage = onImage;
        }

        @Override
        public boolean isInside(final int x, final int y) {
            boolean isInside = super.isInside(x, y);
            if (isInside) {
                if (getImage() == offImage) {
                    setImage(onImage);
                } else {
                    setImage(offImage);
                }
            }
            return isInside;
        }

        /**
         * sets the image to off.
         */
        public void unclick() {
            setImage(offImage);
        }
    }
}
