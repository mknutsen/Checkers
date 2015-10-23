package aiproj.checkers.graphics;

import mknutsen.graphicslibrary.graphicsobject.CircleGraphicObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Created by mknutsen on 10/21/15.
 */
public class CheckersPiece extends CircleGraphicObject {

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

    public final int getRow() {
        return row;
    }

    public final void setRow(int row) {
        this.row = row;
    }

    public final int getCol() {
        return col;
    }

    public final void setCol(int col) {
        this.col = col;
    }

    public final boolean getPlayer1() {
        return player1;
    }
}
