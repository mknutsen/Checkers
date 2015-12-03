package aiproj.checkers.graphics;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.game.player.AIPlayer;
import aiproj.checkers.game.player.Player;
import mknutsen.graphicslibrary.GraphicsComponent;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * game component handles interfacing the board and the players as well as displaying the graphics
 */
public class GameComponent extends GraphicsComponent {

    private final Player player1;

    private final Player player2;

    private CheckersBoard board;

    private AIPlayer aiPlayer1, aiPlayer2;

    private boolean thinking;

    /**
     * Default constructor initializes the board and the players and the mouse listener.
     */
    public GameComponent(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
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
        g.setFont(new Font("Ariel", Font.BOLD, 60));
        g.drawImage(Config.backgroundImage, 0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, null);
        board.draw(g);
        //        System.out.println(board);
        int win = board.checkWin();

        if (win != 0) {
            triggerCallback(win, board);
        }
        if ((aiPlayer1 != null && aiPlayer1.isThinking()) || (aiPlayer2 != null && aiPlayer2.isThinking()) ||
                thinking) {

            g.setColor(
                    new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            g.drawString("Thinking...", 20, 100);
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
        //get and cast the heuristic to use
        StartScreen.Heuristic heuristic = (StartScreen.Heuristic) obj[0];
        System.out.println(heuristic);
        //instantiate the game
        board = new CheckersBoard();
        board.newGame(heuristic);

        //give the players the ability to call repaint
        RepaintCallback callback = new RepaintCallback() {

            @Override
            public void repaint() {
                //                repaint();
            }
        };
        player1.setRepaintCallback(callback);
        player2.setRepaintCallback(callback);

        //setup ai players so it can track the thinking
        if (player1 instanceof AIPlayer) {
            aiPlayer1 = (AIPlayer) player1;
        }
        if (player2 instanceof AIPlayer) {
            aiPlayer2 = (AIPlayer) player2;

        }

        //give it to the players
        player1.setGame(board, true);
        player2.setGame(board, false);

        //set up switching turns
        {
            board.setCallback(new TurnCallback() {

                @Override
                public void nextTurn(final boolean isPlayer1Turn) {
                    CheckersBoard.Move move;
                    if (Config.DEBUG) {
                        System.out.println(board);
                    }
                    if (isPlayer1Turn) {
                        move = player1.triggerTurn();
                    } else {
                        move = player2.triggerTurn();
                    }
                    // if the thing is a computer itll spit out a move to make, make the move
                    if (move != null) {
                        if (Config.DEBUG) {
                            System.out.println("making this move: " + move);
                        }
                        boolean moveMade =
                                board.makeMove(board.getPiece(move.getPiece().getRow(), move.getPiece().getCol()),
                                        move.getEndCell());
                        if (Config.DEBUG) {
                            System.out.println("move made was successful: " + move + " " + moveMade);
                        }
                    }
                }
            });
        }
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
