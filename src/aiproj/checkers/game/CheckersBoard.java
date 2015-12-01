package aiproj.checkers.game;

import aiproj.checkers.graphics.CheckersPiece;
import aiproj.checkers.graphics.Config;
import aiproj.checkers.graphics.GameComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * contains the board with the ability to modify it through a few moves.
 */
public class CheckersBoard {

    private String movesMade = "";

    private boolean player1 = true;
    
    private CheckersPiece[][] checkersBoard;
    
    private ArrayList<CheckersPiece> player1Pieces, player2Pieces;
    
    private GameComponent.TurnCallback callback;
    
    private int winner;
    
    /**
     * gets moves available at that location given a color NOT including the board.
     *
     * @param row
     *         row to look at
     * @param col
     *         column to look at
     * @param player1
     *         color of the piece there
     * @return a list of moves a piece of that color and that place could go
     */
    public static MovesList getMoves(int row, int col, boolean player1, boolean isKing) {
        MovesList nextMoves = new MovesList();
        if (player1 || isKing) {
            nextMoves.add(row + 1, col - 1);
            nextMoves.add(row + 1, col + 1);
        }
        if (!player1 || isKing) {
            nextMoves.add(row - 1, col + 1);
            nextMoves.add(row - 1, col - 1);
        }
        return nextMoves;
    }
    
    /**
     * gets moves available at that location given a color NOT including the board.
     *
     * @param coordinate
     *         location to look at
     * @param player1
     *         color of the piece
     * @return a list of moves a piece of that color and that place could go
     */
    public static MovesList getMoves(Coordinate coordinate, boolean player1, boolean isKing) {
        return CheckersBoard.getMoves(coordinate.getRow(), coordinate.getCol(), player1, isKing);
    }
    
    /**
     * returns the cell that the mouse event could be at
     *
     * @param e
     *         mouse event to locate
     * @return the cell/coordinate the mouse event was in
     */
    public static Coordinate getCell(final MouseEvent e) {
        return new Coordinate(e.getX() / Config.COLUMN_WIDTH, e.getY() / Config.ROW_HEIGHT);
    }
    
    public static void main(String[] args) {
        CheckersBoard board = new CheckersBoard();
        board.newGame();
        if (Config.DEBUG) {
            System.out.println(board);
        }
        
    }
    
    /**
     * gets moves available at that location  NOT including the board or the color, just checkers rules for moving
     * diagonally.
     *
     * @param row
     *         row to look at
     * @param col
     *         column to look at
     * @return a list of moves a piece at that location could move to
     */
    public static MovesList getMoves(int row, int col) {
        MovesList nextMoves = new MovesList();
        
        nextMoves.add(row + 1, col + 1);
        nextMoves.add(row - 1, col + 1);
        nextMoves.add(row + 1, col - 1);
        nextMoves.add(row - 1, col - 1);
        
        return nextMoves;
    }
    
    public final int score() {
        int score;
        if (winner != 0) {
            score = Config.WINNING_VALUE * winner;
        } else {
            score = player1Pieces.size() - player2Pieces.size();

            for (CheckersPiece piece : player1Pieces) {
                score += piece.getIsKing() ? Config.KING_WORTH : 0;
            }
            for (CheckersPiece piece : player2Pieces) {
                score += piece.getIsKing() ? -1 * Config.KING_WORTH : 0;
            }
        }
        return score;
    }
    
    public final boolean isPlayer1() {
        return player1;
    }
    
    public final ArrayList<CheckersPiece> getPlayer1Pieces() {
        return player1Pieces;
    }
    
    public ArrayList<CheckersPiece> getPlayer2Pieces() {
        return player2Pieces;
    }
    
    /**
     * gets the move a piece could actually make NOT including jumps
     *
     * @return the list of moves that piece could make incuding the surroundings
     */
    public final ArrayList<Coordinate> removeImpossibleMoves(MovesList availableMoves) {
        MovesList realMoves = new MovesList();
        if (availableMoves != null) {
            for (Coordinate coordinate : availableMoves) {
                CheckersPiece piece = getPiece(coordinate);
                if (piece == null) {
                    realMoves.add(coordinate);
                }
            }
        }
        return realMoves.list;
    }
    
    /**
     * This method sucks im sorry. why the fuck doesnt this work.
     *
     * @param myPiece
     *         piece to check for jump locations at
     * @param availableMoves
     *         available moves for it to make
     * @return a hashtable. key is a coordinate for the location the piece ends up and the value is the coordinates of
     * the pieces that need to be deleted if the piece lands there.
     */
    public final Hashtable<Coordinate, ArrayList<Coordinate>> getJumpLocations(CheckersPiece myPiece,
                                                                               MovesList availableMoves) {
        myPiece = getPiece(myPiece.getRow(), myPiece.getCol());
        Hashtable<Coordinate, ArrayList<Coordinate>> deleteTable = new Hashtable<>();
        MovesList realMoves = new MovesList();
        for (Coordinate adjacentCell : availableMoves) {
            CheckersPiece piece = getPiece(adjacentCell);
            if (piece != null && piece.getIsPlayer1() != myPiece.getIsPlayer1()) {
                ArrayList<Coordinate> toDelete = new ArrayList<>();
                toDelete.add(adjacentCell);
                Coordinate jumpTo = new Coordinate(myPiece.getRow() + (adjacentCell.row - myPiece.getRow()) * 2,
                        myPiece.getCol() + (adjacentCell.col - myPiece.getCol()) * 2);
                deleteTable.put(jumpTo, toDelete);
                
                ArrayList<Coordinate> toDeleteClone = new ArrayList<>();
                toDelete.forEach(coordinate -> toDeleteClone.add((Coordinate) coordinate.clone()));
                
                checkForJumps(jumpTo.row, jumpTo.col, player1, realMoves, deleteTable, toDeleteClone);
            }
        }
        return deleteTable;
    }
    
    /**
     * gets the piece that mouse event e is on
     *
     * @param e
     *         mouse event on the board
     * @return null if no piece is there
     */
    public final CheckersPiece getPiece(MouseEvent e) {
        Coordinate cell = getCell(e);
        CheckersPiece piece = getPiece(cell.row, cell.col);
        if (piece != null) {
            return piece.isInside(e) ? piece : null;
        }
        return null;
    }
    
    /**
     * recursive alrogithm to check for locations the player can jump to
     *
     * @param row
     *         checks for jumps from this row
     * @param col
     *         checks for jumps from this column
     * @param player1
     *         the color of piece jumping around
     * @return list of locations to jump to
     */
    private void checkForJumps(int row, int col, boolean player1, MovesList moves,
                               Hashtable<Coordinate, ArrayList<Coordinate>> deleteTable,
                               ArrayList<Coordinate> toDelete) {
        Coordinate currentLoc = new Coordinate(row, col);
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            deleteTable.remove(currentLoc);
            return;
        } else if (getPiece(currentLoc) == null) {
            if (!moves.contains(currentLoc)) {
                moves.add(currentLoc);
                for (Coordinate adjacentCell : getMoves(row, col)) {
                    Coordinate jumpTo =
                            new Coordinate(row + (adjacentCell.row - row) * 2, col + (adjacentCell.col - col) * 2);
                    CheckersPiece adjacentPiece = getPiece(adjacentCell);
                    if (adjacentPiece != null && adjacentPiece.getIsPlayer1() != player1) {
                        toDelete.add(adjacentCell);
                        deleteTable.put(jumpTo, toDelete);
                        
                        ArrayList<Coordinate> toDeleteClone = new ArrayList<>();
                        toDelete.forEach(coordinate -> toDeleteClone.add((Coordinate) coordinate.clone()));
                        
                        checkForJumps(jumpTo.row, jumpTo.col, player1, moves, deleteTable, toDeleteClone);
                    }
                }
            }
        } else {
            deleteTable.remove(currentLoc);
        }
    }
    
    /**
     * gets the piece on the board at row,col
     *
     * @param row
     *         row to get piece from
     * @param col
     *         col to get piece from
     * @return piece at row,col
     */
    public final CheckersPiece getPiece(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException(row + ", " + col + " is not okay ");
        }
        return checkersBoard[row][col];
    }
    
    /**
     * @param coordinate
     *         location on board
     * @return piece at that location
     */
    public final CheckersPiece getPiece(Coordinate coordinate) {
        return getPiece(coordinate.row, coordinate.col);
    }
    
    /**
     * sets up board with new pieces
     */
    public final void newGame() {
        player1Pieces = new ArrayList<>();
        player2Pieces = new ArrayList<>();
        
        checkersBoard = new CheckersPiece[8][8];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int row = i;
                int col = 2 * j + ((i + 1) % 2);
                
                checkersBoard[row][col] = new CheckersPiece(row, col, true);
                player1Pieces.add(checkersBoard[row][col]);
                
                row = 7 - i;
                col = 2 * j + (i % 2);
                
                checkersBoard[row][col] = new CheckersPiece(row, col, false);
                player2Pieces.add(checkersBoard[row][col]);
                
            }
        }
    }
    
    /**
     * draws board
     *
     * @param g
     *         graphics with which to draw it
     */
    public final void draw(Graphics g) {
        player1Pieces.forEach(piece -> piece.draw(g));
        player2Pieces.forEach(piece -> piece.draw(g));
    }
    
    /**
     * @return board as a string
     */
    public final String toString() {
        String string = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (checkersBoard[i][j] == null) {
                    string += "[ ]";
                } else if (checkersBoard[i][j].getIsPlayer1()) {
                    string += "[X]";
                } else if (!checkersBoard[i][j].getIsPlayer1()) {
                    string += "[O]";
                }
            }
            string += "\n";
        }
        string += "Score: " + score();
        return string;
    }
    
    /**
     * makes the move if its possible
     * <p>
     * only allow single jumps.
     *
     * @param movingPiece
     *         piece to move
     * @param endCell
     *         place to move it to
     * @return true if move was successful and move was made. false otherwise.
     */
    public final boolean makeMove(CheckersPiece movingPiece, final Coordinate endCell) {
        if (winner != 0) {
            if (Config.DEBUG) {
                System.out.println("the game is over");
            }
            return false;
        }
        movingPiece = getPiece(movingPiece.getRow(), movingPiece.getCol());
        if (Config.DEBUG) {
            System.out.println(movingPiece + " to  " + endCell);
        }
        if (movingPiece.getIsPlayer1() != player1) {
            return false;
        }
        MovesList availableMoves = getMoves(movingPiece.getRow(), movingPiece.getCol(), movingPiece.getIsPlayer1(),
                movingPiece.getIsKing());
        if (removeImpossibleMoves(availableMoves).contains(endCell)) {
            movesMade += movingPiece.toString() + " to " + endCell.toString() + "\n";
            checkersBoard[movingPiece.getRow()][movingPiece.getCol()] = null;
            movingPiece.setCol(endCell.col);
            movingPiece.setRow(endCell.row);
            checkersBoard[endCell.getRow()][endCell.getCol()] = movingPiece;
            checkIfPieceShouldBeKing(endCell.getRow(), endCell.getCol());
            nextTurn();
            return true;
        } else if (getOneJumps(movingPiece, availableMoves).contains(endCell)) {
            final int newCol = movingPiece.getCol() + (endCell.getCol() - movingPiece.getCol()) / 2;
            final int newRow = movingPiece.getRow() + (endCell.getRow() - movingPiece.getRow()) / 2;

            movesMade +=
                    movingPiece.toString() + " to " + endCell.toString() + " taking " + newRow + ", " + newCol + "\n";

            deletePiece(new Coordinate(newRow, newCol));
            
            checkersBoard[movingPiece.getRow()][movingPiece.getCol()] = null;
            movingPiece.setCol(endCell.col);
            movingPiece.setRow(endCell.row);
            checkersBoard[endCell.getRow()][endCell.getCol()] = movingPiece;
            checkIfPieceShouldBeKing(endCell.getRow(), endCell.getCol());
            nextTurn();
            return true;
        }
        //        Hashtable<Coordinate, ArrayList<Coordinate>> deleteTable = getJumpLocations(movingPiece,
        // availableMoves);
        //        ArrayList<Coordinate> deleteList = deleteTable.get(endCell);
        //        if (deleteList != null) {
        //            checkersBoard[movingPiece.getRow()][movingPiece.getCol()] = null;
        //            movingPiece.setCol(endCell.col);
        //            movingPiece.setRow(endCell.row);
        //            checkersBoard[endCell.getRow()][endCell.getCol()] = movingPiece;
        //            deleteList.forEach(toDelete -> {
        //                if (!deletePiece(toDelete)) {
        //                    System.err.println("CAN'T DELETE: " + toDelete + " from " + deleteList);
        //                }
        //            });
        //            nextTurn();
        //            return true;
        //        }
        return false;
    }
    
    public final ArrayList<Coordinate> getOneJumps(final CheckersPiece piece, final MovesList availableMoves) {
        MovesList newList = new MovesList();
        for (Coordinate coordinate : availableMoves) {
            CheckersPiece tempPiece = getPiece(coordinate);
            if (tempPiece != null && tempPiece.getIsPlayer1() != piece.getIsPlayer1()) {
                final int newCol = piece.getCol() + (tempPiece.getCol() - piece.getCol()) * 2;
                final int newRow = piece.getRow() + (tempPiece.getRow() - piece.getRow()) * 2;
                final CheckersPiece newPiece;
                try {
                    newPiece = getPiece(newRow, newCol);
                    if (Config.DEBUG) {
                        System.out.println(newRow + ", " + newCol);
                    }
                    if (newPiece == null) {
                        newList.add(newRow, newCol);
                    } else {
                        if (Config.DEBUG) {
                            System.out.println("THERES SOMETHING THERE: " + newPiece);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    if (Config.DEBUG) {
                        System.err.println(newRow + ", " + newCol + " tried to jump there");
                    }
                }
            }
        }
        return newList.list;
    }
    
    /**
     * @param row
     *         row of piece
     * @param col
     *         column of piece
     * @return true if piece was kinged
     */
    private boolean checkIfPieceShouldBeKing(final int row, final int col) {
        CheckersPiece piece = getPiece(row, col);
        if (piece.getIsPlayer1()) {
            if (row == 7) {
                piece.king();
                return true;
            }
        } else {
            if (row == 0) {
                piece.king();
                return true;
            }
        }
        return false;
    }
    
    private boolean deletePiece(final Coordinate toDelete) {
        CheckersPiece tempPiece = checkersBoard[toDelete.row][toDelete.col];
        if (Config.DEBUG) {
            System.out.println("to delete at " + tempPiece);
        }
        if (tempPiece == null) {
            return false;
        }
        if (tempPiece.getIsPlayer1()) {
            player1Pieces.remove(tempPiece);
        } else {
            player2Pieces.remove(tempPiece);
        }
        checkersBoard[toDelete.row][toDelete.col] = null;
        
        return true;
        
    }
    
    /**
     * checks to see if anyone won
     *
     * @return 0  if the game is still going on. 1 if player 1 won, -1 if player 2 won
     */
    public final int checkWin() {
        boolean player1 = false, player2 = false;
        if (winner == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (checkersBoard[i][j] != null) {
                        if (checkersBoard[i][j].getIsPlayer1()) {
                            player1 = true;
                        } else {
                            player2 = true;
                        }
                        if (player1 && player2) {
                            return 0;
                        }
                    }
                }
            }
            winner = player1 ? 1 : -1;
        }
        return winner;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public Object clone() {
        //new board
        CheckersBoard board = new CheckersBoard();

        //initialize everything
        board.player1Pieces = new ArrayList<>();
        board.player2Pieces = new ArrayList<>();
        board.checkersBoard = new CheckersPiece[8][8];

        //copy pieces
        player1Pieces.forEach(piece -> board.player1Pieces.add((CheckersPiece) piece.clone()));
        player2Pieces.forEach(piece -> board.player2Pieces.add((CheckersPiece) piece.clone()));

        //copy turn
        board.player1 = player1;

        //put the pieces on the board
        board.player1Pieces.forEach(piece -> board.checkersBoard[piece.getRow()][piece.getCol()] = piece);
        board.player2Pieces.forEach(piece -> board.checkersBoard[piece.getRow()][piece.getCol()] = piece);

        //board callback being null might be dirty
        board.callback = null;

        board.movesMade = movesMade;

        return board;
    }
    
    private void nextTurn() {
        player1 = !player1;
        if (callback != null) {
            callback.nextTurn(player1);
        }
    }
    
    public final void setCallback(GameComponent.TurnCallback callback) {
        this.callback = callback;
    }
    
    public final void declareWinner(final boolean winner) {
        this.winner = winner ? 1 : -1;
    }

    public final String getBoardString() {
        if (Config.DEBUG) {
            System.out.println("MOVE MADE " + movesMade);
        }
        return movesMade;
    }

    /**
     * @param player1
     *         player 1 or player2
     * @return list of available moves
     */
    public final java.util.List<Move> getMovesForPlayer(boolean player1) {
        java.util.List<Move> moves = new ArrayList<>();
        ArrayList<CheckersPiece> myPieces = player1 ? getPlayer1Pieces() : getPlayer2Pieces();
        if (!myPieces.isEmpty()) {
            for (CheckersPiece piece : myPieces) {
                CheckersBoard.MovesList moveList =
                        CheckersBoard.getMoves(piece.getRow(), piece.getCol(), player1, piece.getIsKing());
                removeImpossibleMoves(moveList).forEach(move -> moves.add(new Move(piece, move)));
                getOneJumps(piece, moveList).forEach(move -> moves.add(new Move(piece, move)));
            }
        }
        return moves;
    }
    
    /**
     * class for a location on the board
     */
    public static final class Coordinate {

        private int row;

        private int col;

        /**
         * @param row
         *         row of the location
         * @param col
         *         column of the location
         */
        public Coordinate(int row, int col) {
            this.setRow(row);
            this.setCol(col);
        }

        /**
         * @return the row
         */
        public int getRow() {
            return row;
        }

        /**
         * @param row
         *         sets the row
         */
        public void setRow(int row) {
            this.row = row;
        }

        /**
         * @return the col
         */
        public int getCol() {
            return col;
        }

        /**
         * @param col
         *         sets the col
         */
        public void setCol(int col) {
            this.col = col;
        }

        @Override
        public int hashCode() {
            return row * 10 + col;
        }

        @Override
        protected Object clone() {
            return new Coordinate(row, col);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Coordinate) {
                Coordinate coor = (Coordinate) obj;
                return row == coor.row && col == coor.col;
            }
            return false;
        }

        /**
         * @return a string representing the location
         */
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }
    
    /**
     * a modified arraylist that only accepts coordinates that are in the boards range.
     */
    public static class MovesList implements Iterable<Coordinate> {

        private ArrayList<Coordinate> list = new ArrayList<>();

        /**
         * creates new coordiante at x,y and adds it
         *
         * @param x
         *         x of the location to add
         * @param y
         *         y of the location to add
         */
        public final void add(int x, int y) {
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                list.add(new Coordinate(x, y));
            }
        }

        /**
         * adds coordiante to list
         *
         * @param coordinate
         *         coordiante to add for list
         */
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

        /**
         * @param cell
         *         checks if the list contains the coordinate cell
         * @return true if it contains it
         */
        public final boolean contains(final Coordinate cell) {
            if (Config.DEBUG) {
                System.out.println("why tf this shit null: " + cell);
            }
            for (Coordinate loc : list) {
                if (loc.getRow() == cell.getRow() && loc.getCol() == cell.getCol()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public final String toString() {
            return list.toString();
        }
    }

    /**
     * a move contains a piece to be moved and a cell to move the piece to
     */
    public static final class Move {

        private CheckersPiece piece;

        private Coordinate move;

        /**
         * @param piece
         *         piece to move
         * @param move
         *         cell to move it to
         */
        public Move(final CheckersPiece piece, final Coordinate move) {

            this.piece = piece;
            this.move = move;
        }

        public CheckersPiece getPiece() {
            return piece;
        }

        public Coordinate getEndCell() {
            return move;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Move)) {
                return false;
            }

            final Move move1 = (Move) o;

            if (getPiece() != null ? !getPiece().equals(move1.getPiece()) : move1.getPiece() != null) {
                return false;
            }
            return !(move != null ? !move.equals(move1.move) : move1.move != null);

        }

        @Override
        public int hashCode() {
            int result = getPiece() != null ? getPiece().hashCode() : 0;
            result = 31 * result + (move != null ? move.hashCode() : 0);
            return result;
        }

        public final String toString() {
            return piece + " to " + getEndCell();

        }
    }
}
