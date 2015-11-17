package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * implementation of player that makes a move randomly when triggered. this is defintely more complicated than
 * necessary.
 */
public final class RandomPlayer extends Player {
    
    @Override
    public final CheckersBoard.Move triggerTurn() {
        CheckersBoard board = getGame();
        final Hashtable<String, ArrayList<CheckersBoard.Coordinate>> coordinateTable = new Hashtable<>();
        for (CheckersPiece piece : board.getPlayer2Pieces()) {
            ArrayList<CheckersBoard.Coordinate> coordinate = new ArrayList<>();
            board.removeImpossibleMoves(CheckersBoard.getMoves(piece.getRow(), piece.getCol()))
                    .forEach(item -> coordinate.add(item));
            Enumeration<CheckersBoard.Coordinate> list =
                    board.getJumpLocations(piece, CheckersBoard.getMoves(piece.getRow(), piece.getCol())).keys();
            while (list.hasMoreElements()) {
                coordinate.add(list.nextElement());
            }
            if (coordinate.size() > 0) {
                coordinateTable.put(piece.toString(), coordinate);
            }
        }
        System.out.println("Available moves: " + coordinateTable);
        int i = 0;
        int keyNum = (int) (Math.random() * coordinateTable.keySet().size());
        for (String pieceString : coordinateTable.keySet()) {
            int commaIndex = pieceString.indexOf(',');
            CheckersPiece piece = new CheckersPiece(Integer.parseInt(pieceString.substring(commaIndex - 1, commaIndex)),
                    Integer.parseInt(pieceString.substring(commaIndex + 2, commaIndex + 3)),
                    "red".equals(pieceString.substring(0, 3)));
            if (i++ == keyNum) {
                ArrayList<CheckersBoard.Coordinate> list = coordinateTable.get(piece.toString());
                int num = (int) (list.size() * Math.random());
                System.out.println("this happened: " + list + " " + num + " " + list.get(num));

                return new CheckersBoard.Move(piece, list.get(num));
            }
        }
        System.out.println("this isnt supposed to happen");
        return null;
    }
}
