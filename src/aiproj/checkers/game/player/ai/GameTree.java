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
        root = new Node(game, game.score());

    }

    /**
     * takes in a string representing the last move and makes the change to the set of boards in memory
     *
     * @param moveString
     *         move string in the format of "Black at 2, 3 to 3, 4"
     */
    public final void intakeLastMove(String moveString) {
        // remove punctuation and move it to an array
        String[] moveArray = moveString.replaceAll("[^a-zA-Z1-9 ]", "").toLowerCase().split("\\s+");

        //the first move is 2,3 and the second move is 5,6
        CheckersBoard.Coordinate moveFrom =
                new CheckersBoard.Coordinate(Integer.parseInt(moveArray[2]), Integer.parseInt(moveArray[3]));
        CheckersBoard.Coordinate moveTo =
                new CheckersBoard.Coordinate(Integer.parseInt(moveArray[5]), Integer.parseInt(moveArray[6]));
        System.out.println("received last move: " + moveFrom + " " + moveTo);

        //update the tree
        CheckersPiece movingPiece = root.board.getPiece(moveFrom);
        CheckersBoard.Move move = new CheckersBoard.Move(movingPiece, moveTo);
        for (Node node : root.nodeList) {
            if (node.moveUsed.equals(move)) {
                root = node;
                return;
            }
        }
        Node node = new Node((CheckersBoard) root.board.clone(), root.score, move);
        node.board.makeMove(move.getPiece(), move.getEndCell());
        node.score = node.board.score();

        //garbage collection here if possible?

        root = node;


    }

    /**
     * @return the move the computer should make
     */
    public CheckersBoard.Move bestMove() {
        Node max = root.nodeList.get(0);
        for (Node node : root.nodeList) {
            if (max.score < node.score) {
                max = node;
            }
        }
        return max.moveUsed;
    }

    /**
     * @param layersDeep
     *         number of layers to check (-1 will fully populate tree)
     * @param node
     *         node being looked at right now
     */
    public final void populate(int layersDeep, Node node) {
        if (node.board.checkWin() == 0 && layersDeep != 0) {
            if (node.nodeList.isEmpty()) {
                for (CheckersBoard.Move move : node.board.getMovesForPlayer(node.board.isPlayer1())) {
                    CheckersBoard board = (CheckersBoard) node.board.clone();
                    board.makeMove(move.getPiece(), move.getEndCell());
                    node.nodeList.add(new Node(board, board.score()));
                    if (node.board.isPlayer1()) {

                    } else {

                    }
                }
            }
            populate(layersDeep - 1, node);
        }
    }

    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersBoard board;

        final CheckersBoard.Move moveUsed;

        int score;

        Node(final CheckersBoard board, int score) {
            this(board, score, null);
        }

        Node(final CheckersBoard board, int score, CheckersBoard.Move move) {
            this.board = board;
            nodeList = new ArrayList<>();
            this.score = score;
            moveUsed = move;

        }
    }
}
