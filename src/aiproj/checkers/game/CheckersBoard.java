package aiproj.checkers.game;

import aiproj.checkers.graphics.CheckersPiece;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by mknutsen on 10/21/15.
 */
public class CheckersBoard {

    private boolean turn;

    private CheckersPiece[][] checkersBoard = new CheckersPiece[8][8];

    public static final MovesList getMoves(int row, int col, boolean player1) {
        MovesList nextMoves = new MovesList();
        if (player1) {
            nextMoves.add(row + 1, col + 1);
            nextMoves.add(row - 1, col + 1);
        } else {
            nextMoves.add(row + 1, col - 1);
            nextMoves.add(row - 1, col - 1);
        }
        return nextMoves;
    }

    public static final MovesList getMoves(int row, int col) {
        MovesList nextMoves = new MovesList();

        nextMoves.add(row + 1, col + 1);
        nextMoves.add(row - 1, col + 1);
        nextMoves.add(row + 1, col - 1);
        nextMoves.add(row - 1, col - 1);

        return nextMoves;
    }

    public static final MovesList getMoves(Coordinate coordinate, boolean player1) {
        return CheckersBoard.getMoves(coordinate.getRow(), coordinate.getCol(), player1);
    }

    public final MovesList getRealMoves(int row, int col) {
        CheckersPiece myPiece = getPiece(row, col);
        MovesList availableMoves = getMoves(row, col, myPiece.getPlayer1());
        MovesList realMoves = new MovesList();
        for (Coordinate coordinate : availableMoves) {
            CheckersPiece piece = getPiece(coordinate);
            if (piece == null) {
                realMoves.add(coordinate);
            } else if (myPiece.getPlayer1() != piece.getPlayer1()) {
                checkForJumps(row + (coordinate.row - row) * 2, col + (coordinate.col - col) * 2, myPiece.getPlayer1());
            }
        }
        return realMoves;
    }

    private final ArrayList<Coordinate> checkForJumps(int row, int col, boolean player1) {
        return null;
    }

    public final CheckersPiece getPiece(int row, int col) {
        return checkersBoard[col][row];
    }

    public final CheckersPiece getPiece(Coordinate coordinate) {
        return getPiece(coordinate.row, coordinate.col);
    }

    public void newGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int row = i;
                int col = 2 * j + ((i + 1) % 2);
                checkersBoard[col][row] = new CheckersPiece(col, row, true);
                row = 7 - i;
                col = 2 * j + (i % 2);
                checkersBoard[col][row] = new CheckersPiece(col, row, false);
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkersBoard[i][j] != null) {
                    checkersBoard[i][j].draw(g);
                }
            }
        }
    }

    public static final class Coordinate {

        private int row;

        private int col;

        public Coordinate(int row, int col) {
            this.setRow(row);
            this.setCol(col);
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    public static class MovesList implements Iterable<Coordinate> {

        private ArrayList<Coordinate> list = new ArrayList<>();

        public final void add(int x, int y) {
            if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
                list.add(new Coordinate(x, y));
            }
        }

        public final void add(Coordinate coordinate) {
            this.add(coordinate.row, coordinate.col);
        }

        @Override
        public Iterator<Coordinate> iterator() {
            return new Iterator<Coordinate>() {

                private int counter = 0;

                @Override
                public boolean hasNext() {
                    return counter < list.size();
                }

                @Override
                public Coordinate next() {
                    return list.get(counter++);
                }
            };
        }

        @Override
        public void forEach(final Consumer<? super Coordinate> action) {
            list.forEach(item -> action.accept(item));
        }

        @Override
        public Spliterator<Coordinate> spliterator() {
            return null;
        }

        public final Coordinate get(int i) {
            return list.get(i);
        }
    }
}
