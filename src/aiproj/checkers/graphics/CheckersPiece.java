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
            Config.pieces[2] = ImageIO.read(CheckersPiece.class.getResourceAsStream("resource/king.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int row;

    private boolean player1;

    private boolean isKing;

    private int col;

    private int hoverX = -1, hoverY = -1;

    /**
     * @param row
     *         row the checkers piece belongs in
     * @param col
     *         col the checkers piece belongs in
     * @param player1
     *         does the piece belong to player 1
     */
    public CheckersPiece(int row, int col, boolean player1) {
        super(Config.COLUMN_WIDTH * row + Config.X_OFFSET, Config.ROW_HEIGHT * col + Config.Y_OFFSET,
                Config.PIECE_WIDTH / 2, false, player1 ? Config.pieces[0] : Config.pieces[1]);
        this.row = row;
        this.col = col;
        this.player1 = player1;
        this.isKing = false;
    }

    CheckersPiece(int row, int col, boolean player1, boolean isKing) {
        this(row, col, player1);
        this.isKing = isKing;
    }

    public static void main(String[] args) {
        CheckersPiece piece = new CheckersPiece(11, 1, true);
        CheckersPiece piece2 = new CheckersPiece(11, 1, true);
        System.out.println(piece.equals(piece2));
    }

    @Override
    public final boolean draw(final Graphics gr) {
        int x = hoverX >= 0 && hoverY >= 0 ? hoverX : getX();
        int y = hoverX >= 0 && hoverY >= 0 ? hoverY : getY();

        gr.drawImage(getImage(), x - getRadius(), y - getRadius(), getRadius() * 2, getRadius() * 2, null);

        if (isKing) {
            gr.drawImage(Config.pieces[2], x - getRadius(), y - getRadius(), getRadius() * 2, getRadius() * 2 - 10,
                    null);
        }

        return true;
    }

    public final void setHover(int x, int y) {
        this.hoverX = x;
        this.hoverY = y;
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
        setX(Config.COLUMN_WIDTH * row + Config.X_OFFSET);
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
        System.out.println("old col! " + getY());
        setY(Config.ROW_HEIGHT * col + Config.Y_OFFSET);
        System.out.println("new col! " + getY());
    }

    /**
     * @return true if the piece belongs to player one
     */
    public final boolean getIsPlayer1() {
        return player1;
    }

    @Override
    public final int hashCode() {
        return row + col * 10 + (player1 ? 100 : 0);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof CheckersPiece) {
            return this.toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public final Object clone() {
        return new CheckersPiece(row, col, player1, isKing);
    }

    @Override
    public final String toString() {
        return (player1 ? "Red " : "Black ") + "at " + row + ", " + col;
    }

    public final String getDrawLocation() {

        int x = hoverX >= 0 && hoverY >= 0 ? hoverX : getX();
        int y = hoverX >= 0 && hoverY >= 0 ? hoverY : getY();
        return x + ", " + y;
    }

    public final boolean getIsKing() {
        return isKing;
    }

    public final void king() {
        isKing = true;
    }
}
