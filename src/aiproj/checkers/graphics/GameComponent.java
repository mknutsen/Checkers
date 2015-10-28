package aiproj.checkers.graphics;

import aiproj.checkers.game.CheckersBoard;
import mknutsen.graphicslibrary.GraphicsComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * game component handles interfacing the board and the players as well as displaying the graphics
 */
public class GameComponent extends GraphicsComponent implements MouseListener, MouseMotionListener {

    private static BufferedImage background;

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

    private CheckersPiece hover = null;

    private CheckersBoard board;

    /**
     * Default constructor initializes the board and the players and the mouse listener.
     */
    public GameComponent() {
        board = new CheckersBoard();
        board.newGame();
        addMouseListener(this);
        addMouseMotionListener(this);
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
        hover = board.getPiece(e);
        repaint();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (hover != null) {
            board.makeMove(hover, CheckersBoard.getCell(e));
            hover.setHover(-1, -1);
            hover = null;
            int win = board.checkWin();
            if (win != 0) {
                triggerCallback(win);
            } else {
                final Hashtable<String, ArrayList<CheckersBoard.Coordinate>> coordinateTable = new Hashtable<>();
                for (CheckersPiece piece : board.getPlayer2Pieces()) {
                    ArrayList<CheckersBoard.Coordinate> coordinate = new ArrayList<>();
                    board.getRealMoves(piece, CheckersBoard.getMoves(piece.getRow(), piece.getCol()))
                            .forEach(item -> coordinate.add(item));
                    Enumeration<CheckersBoard.Coordinate> list =
                            board.getJumpLocations(piece, CheckersBoard.getMoves(piece.getRow(), piece.getCol()))
                                    .keys();
                    while (list.hasMoreElements()) {
                        coordinate.add(list.nextElement());
                    }
                    if (coordinate.size() > 0) {
                        coordinateTable.put(piece.toString(), coordinate);
                    }
                }
                int i = 0;
                int keyNum = (int) (Math.random() * coordinateTable.keySet().size());
                for (String pieceString : coordinateTable.keySet()) {
                    int commaIndex = pieceString.indexOf(',');
                    CheckersPiece piece =
                            new CheckersPiece(Integer.parseInt(pieceString.substring(commaIndex - 1, commaIndex)),
                                    Integer.parseInt(pieceString.substring(commaIndex + 2, commaIndex + 3)),
                                    "red".equals(pieceString.substring(0, 3)));
                    if (i++ == keyNum) {
                        ArrayList<CheckersBoard.Coordinate> list = coordinateTable.get(piece.toString());
                        board.makeMove(piece, list.get((int) (list.size() * Math.random())));
                        win = board.checkWin();
                        if (win != 0) {
                            triggerCallback(win);
                        }
                    }
                }
            }
            repaint();
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (hover != null) {
            hover.setHover(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }
}
