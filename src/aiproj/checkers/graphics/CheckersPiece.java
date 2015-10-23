package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.graphicsobject.CircleGraphicObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


/**
 * CheckersPiece is a piece on the board that understands where it sits on the board and is either red or black
 * depending on the player.
 */
public class CheckersPiece extends CircleGraphicObject {

    /**
     * Responsible for loading the images for the checkers pieces
     */
    static {
        try {
            Config.pieces[0] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/red.png"));
            Config.pieces[1] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/black.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int row;

    private boolean player1;

    private int col;

    /**
     * @param row
     *         row the checkers piece belongs in
     * @param col
     *         col the checkers piece belongs in
     * @param player1
     *         does the piece belong to player 1
     */
    public CheckersPiece(int row, int col, boolean player1) {
        super(Config.COLUMN_WIDTH * row, Config.ROW_HEIGHT * col, Config.PIECE_WIDTH / 2, false,
                player1 ? Config.pieces[0] : Config.pieces[1]);
        this.row = row;
        this.col = col;
        this.player1 = player1;
    }

    @Override
    public final boolean draw(final Graphics gr) {
        gr.drawImage(getImage(), getX() - getRadius() + Config.X_OFFSET, getY() - getRadius() + Config.Y_OFFSET,
                getRadius() * 2, getRadius() * 2, null);
        return true;
    }

    /**
     * @return row of the piece
     */
    public final int getRow() {
        return row;
    }

    /**
     * @param row
     *         new row the for the piece
     */
    public final void setRow(int row) {
        this.row = row;
    }

    /**
     * @return col of the piece
     */
    public final int getCol() {
        return col;
    }

    /**
     * @param col
     *         new col of the piece
     */
    public final void setCol(int col) {
        this.col = col;
    }

    /**
     * @return true if the piece belongs to player one
     */
    public final boolean getIsPlayer1() {
        return player1;
    }
}
