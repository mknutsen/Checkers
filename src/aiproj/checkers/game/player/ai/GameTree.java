package aiproj.checkers.game.player.ai;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;
import aiproj.checkers.graphics.Config;

import java.util.ArrayList;

/**
 * Created by mknutsen on 11/24/15.
 */
public class GameTree {

    private Node root;

    private boolean thinking;

    public GameTree(final CheckersBoard game) {
        root = new Node(game);
        Runnable populate = () -> {
            thinking = true;
            populate(Config.AI_DEPTH, root);
            System.out.println("were good fam");
            thinking = false;
        };
        new Thread(populate).start();
    }

    /**
     * takes in a string representing the last move and makes the change to the set of boards in memory
     *
     * @param moveString
     *         move string in the format of "Black at 2, 3 to 3, 4"
     */
    public final void intakeLastMove(String moveString) {
        // remove punctuation and move it to an array
        String[] moveArray = moveString.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");

        CheckersBoard.Coordinate moveFrom = null, moveTo = null;
        //the first move is 2,3 and the second move is 5,6
        try {
            moveFrom = new CheckersBoard.Coordinate(Integer.parseInt(moveArray[2]), Integer.parseInt(moveArray[3]));
            moveTo = new CheckersBoard.Coordinate(Integer.parseInt(moveArray[5]), Integer.parseInt(moveArray[6]));
            if (Config.DEBUG) {
                System.out.println("received last move: " + moveFrom + " " + moveTo);
            }
        } catch (NumberFormatException e) {
            System.err.print("fuck: " + moveString + " [");
            for (String item : moveArray) {
                System.err.print(item + ", ");
            }
            System.err.println("]");
            System.exit(0);
        }

        //update the tree
        CheckersPiece movingPiece = root.board.getPiece(moveFrom);
        CheckersBoard.Move move = new CheckersBoard.Move(movingPiece, moveTo);
        for (Node node : root.nodeList) {
            if (node.moveUsed.equals(move)) {
                root = node;
                return;
            }
        }
        Node node = new Node((CheckersBoard) root.board.clone(), move);
        node.board.makeMove(move.getPiece(), move.getEndCell());
        node.score = node.board.score();

        //garbage collection here if possible?

        root = node;

        populate(Config.AI_DEPTH, root);
    }

    /**
     * @return the move the computer should make
     */
    public CheckersBoard.Move bestMove() {
        if (root.nodeList.isEmpty()) {
            return null;
        }
        Node max = root.nodeList.get(0);
        if (root.board.isPlayer1()) {
            for (Node node : root.nodeList) {
                if (max.score < node.score) {
                    max = node;
                }
            }
        } else {
            for (Node node : root.nodeList) {
                if (max.score > node.score) {
                    max = node;
                }
            }
        }
        if (Config.DEBUG) {
            System.out.println("Move to use: " + max.moveUsed);
        }
        root = max;
        populate(Config.AI_DEPTH, root);
        return max.moveUsed;
    }

    /**
     * @param layersDeep
     *         number of layers to check (-1 will fully populate tree)
     * @param node
     *         node being looked at right now
     */
    public final void populate(int layersDeep, Node node) {
        if (Config.DEBUG) {
            System.out.println("starting with " + node + " and have " + layersDeep + " left");
        }
        if (node.board.checkWin() == 0 && layersDeep != 0) {
            if (node.nodeList.isEmpty()) {
                for (CheckersBoard.Move move : node.board.getMovesForPlayer(node.board.isPlayer1())) {
                    CheckersBoard board = (CheckersBoard) node.board.clone();
                    board.makeMove(move.getPiece(), move.getEndCell());

                    Node newNode = new Node(board, move);
                    node.nodeList.add(newNode);
                }
                for (Node newNode : node.nodeList) {
                    populate(layersDeep - 1, newNode);


                    if (node.board.isPlayer1()) {
                        if (node.score < newNode.score) {
                            node.score = newNode.score;
                        }
                    } else {
                        if (node.score > newNode.score) {
                            node.score = newNode.score;
                        }
                    }
                }
            }
        } else {
            node.score = node.board.score();
        }
    }
    
    public boolean isThinking() {
        return thinking;
    }
    
    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersBoard board;

        final CheckersBoard.Move moveUsed;

        int score;

        Node(final CheckersBoard board) {
            this(board, null);
        }

        Node(final CheckersBoard board, CheckersBoard.Move move) {
            this.board = board;
            nodeList = new ArrayList<>();
            moveUsed = move;
            score = board.isPlayer1() ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        }

        public final String toString() {
            return moveUsed == null ? "no move" : moveUsed.toString() + ", score " + score;
        }
    }
}
