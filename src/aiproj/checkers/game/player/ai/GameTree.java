package aiproj.checkers.game.player.ai;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;

import java.util.ArrayList;

/**
 * Created by mknutsen on 11/24/15.
 */
public class GameTree {

    Node root;

    public GameTree(final CheckersBoard game) {
        root = new Node(game);
    }

    /**
     * takes in a string representing the last move and makes the change to the set of boards in memory
     *
     * @param move
     *         move string in the format of "Black at 2, 3 to 3, 4"
     */
    public final static void intakeLastMove(String move) {
        // remove punctuation and move it to an array
        String[] moveArray = move.replaceAll("[^a-zA-Z1-9 ]", "").toLowerCase().split("\\s+");

        //the first move is 2,3 and the second move is 5,6
        CheckersBoard.Coordinate moveFrom =
                new CheckersBoard.Coordinate(Integer.parseInt(moveArray[2]), Integer.parseInt(moveArray[3]));
        CheckersBoard.Coordinate moveTo =
                new CheckersBoard.Coordinate(Integer.parseInt(moveArray[5]), Integer.parseInt(moveArray[6]));
        System.out.println("received last move: " + moveFrom + " " + moveTo);

        //update the tree

    }

    /**
     * @return the move the computer should make
     */
    public CheckersBoard.Move bestMove() {
        CheckersPiece piece = null;
        CheckersBoard.Coordinate endLocation = null;
        return new CheckersBoard.Move(piece, endLocation);
    }

    /**
     * populate the tree fully
     */
    public final void populate() {

    }

    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersBoard board;

        Node(final CheckersBoard board) {
            this.board = board;
            nodeList = new ArrayList<>();
        }
    }
}
