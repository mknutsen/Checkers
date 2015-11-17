package aiproj.checkers.graphics;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.game.player.Player;
import mknutsen.graphicslibrary.GraphicsComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * game component handles interfacing the board and the players as well as displaying the graphics
 */
public class GameComponent extends GraphicsComponent {

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

    int i = 0;

    private CheckersBoard board;

    /**
     * Default constructor initializes the board and the players and the mouse listener.
     */
    public GameComponent(Player player1, Player player2) {
        //instantiate the game
        board = new CheckersBoard();
        board.newGame();

        //give it to the players
        player1.setGame(board, true);
        player2.setGame(board, false);

        //give the players the ability to call repaint
        RepaintCallback callback = new RepaintCallback() {

            @Override
            public void repaint() {
                //                repaint();
            }
        };
        player1.setRepaintCallback(callback);
        player2.setRepaintCallback(callback);

        //set up switching turns
        board.setCallback(new TurnCallback() {

            @Override
            public void nextTurn(final boolean isPlayer1Turn) {
                CheckersBoard.Move move;
                System.out.println(board);
                if (isPlayer1Turn) {
                    move = player1.triggerTurn();
                } else {
                    move = player2.triggerTurn();
                }
                // if the thing is a computer itll spit out a move to make, make the move
                if (move != null) {
                    System.out.println("making this move: " + move);
                    board.makeMove(board.getPiece(move.getPiece().getRow(), move.getPiece().getCol()),
                            move.getEndCell());
                }
            }
        });
        //add the appropriate listeners to player1
        if (player1 instanceof MouseListener) {
            addMouseListener((MouseListener) player1);
        }
        if (player1 instanceof MouseMotionListener) {
            addMouseMotionListener((MouseMotionListener) player1);
        }
        if (player1 instanceof KeyListener) {
            addKeyListener((KeyListener) player1);
        }

        //add the appropriate listeners to player2
        if (player2 instanceof MouseListener) {
            addMouseListener((MouseListener) player2);
        }
        if (player2 instanceof MouseMotionListener) {
            addMouseMotionListener((MouseMotionListener) player2);
        }
        if (player2 instanceof KeyListener) {
            addKeyListener((KeyListener) player2);
        }
    }

    @Override
    public void paint(final Graphics g) {
        //        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random()
        // * 255)));
        //        g.fillRect(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        g.drawImage(background, 0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, null);
        board.draw(g);
        //        System.out.println(board);
        int win = board.checkWin();

        if (win != 0) {
            triggerCallback(win);
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    public void takeParameters(final Object[] obj) {

    }

    public static abstract class TurnCallback {

        /**
         * @param isPlayer1Turn
         *         whos turn is it now
         */
        public abstract void nextTurn(boolean isPlayer1Turn);
    }

    public static abstract class RepaintCallback {

        public abstract void repaint();
    }
}
