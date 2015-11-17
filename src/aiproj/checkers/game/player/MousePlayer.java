package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Player controlled by a mouse
 */
public class MousePlayer extends Player implements MouseListener, MouseMotionListener {


    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        CheckersBoard board = getGame();
        CheckersPiece piece = board.getPiece(e);

        setHover(piece);

    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (getHover() != null) {
            makeMove(getHover(), CheckersBoard.getCell(e));
            setHover(new CheckersBoard.Coordinate(-1, -1));
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
        setHover(new CheckersBoard.Coordinate(e.getX(), e.getY()));

    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }

    @Override
    public CheckersBoard.Move triggerTurn() {
        return null;
    }
}
