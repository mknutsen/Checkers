package aiproj.checkers.game.player.ai;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.CheckersPiece;
import aiproj.checkers.graphics.Config;
import aiproj.checkers.graphics.StartScreen;

import java.util.ArrayList;

/**
 * MiniMax Tree with Alpha Beta Pruning
 */
public class GameTree {
    
    private Node root;
    
    private boolean thinking;

    private StartScreen.Heuristic heuristic;

    /**
     * @param game
     *         the game to build the tree out of
     * @param heuristic
     *         the hueristic to use to grade the game
     */
    public GameTree(final CheckersBoard game, StartScreen.Heuristic heuristic) {
        root = new Node(game);
        Runnable populate = () -> {
            thinking = true;
            populate(Config.AI_DEPTH, root, root.board.isPlayer1() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            System.out.println("Game Tree done building");
            thinking = false;
            this.heuristic = heuristic;
            
        };
        new Thread(populate).start();
    }

    /**
     * @param board
     *         board to score
     * @param heuristic
     *         heuristic to use
     * @return score of that game
     */
    private static int score(CheckersBoard board, StartScreen.Heuristic heuristic) {
        if (heuristic == null) {
            return board.score();
        } else if (heuristic == StartScreen.Heuristic.A) {
            return board.scoreA();
        } else if (heuristic == StartScreen.Heuristic.B) {
            return board.scoreB();
        } else {
            return board.scoreC();
        }
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
                populate(Config.AI_DEPTH, root, root.board.isPlayer1() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                return;
            }
        }
        Node node = new Node((CheckersBoard) root.board.clone(), move);
        node.board.makeMove(move.getPiece(), move.getEndCell());
        node.score = score(node.board, heuristic);

        //garbage collection here if possible?

        root = node;

        populate(Config.AI_DEPTH, root, root.board.isPlayer1() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
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
        populate(Config.AI_DEPTH, root, root.board.isPlayer1() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        return max.moveUsed;
    }
    
    /**
     * @param layersDeep
     *         number of layers to check (-1 will fully populate tree)
     * @param node
     *         node being looked at right now
     */
    public final void populate(int layersDeep, Node node, int parentValue) {
        if (node.board.checkWin() == 0 && layersDeep != 0) {
            if (node.nodeList.isEmpty()) {
                for (CheckersBoard.Move move : node.board.getMovesForPlayer(node.board.isPlayer1())) {
                    CheckersBoard board = (CheckersBoard) node.board.clone();
                    board.makeMove(move.getPiece(), move.getEndCell());

                    Node newNode = new Node(board, move);
                    node.nodeList.add(newNode);
                }
                for (Node newNode : node.nodeList) {
                    if ((!node.board.isPlayer1() && parentValue <= node.score) ||
                            (node.board.isPlayer1() && parentValue >= node.score)) {
                        populate(layersDeep - 1, newNode, node.score);
                    } else if (Config.DEBUG) {
                        System.out.println("PRUNED: " + node);
                    }


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
            node.score = score(node.board, heuristic);
        }
        if (Config.DEBUG) {
            System.out.println("ending  with " + node + " and have " + layersDeep + " left");
        }
    }
    
    public boolean isThinking() {
        return thinking;
    }

    /**
     * Node contains a board, the move used to get there, and the list of moves coming after.
     */
    private class Node {

        final ArrayList<Node> nodeList;

        final CheckersBoard board;

        final CheckersBoard.Move moveUsed;

        int score;

        /**
         * @param board
         *         builds a node around the board
         */
        Node(final CheckersBoard board) {
            this(board, null);
        }

        /**
         * @param board
         *         board to live at node
         * @param move
         *         move used to get to this game state
         */
        Node(final CheckersBoard board, CheckersBoard.Move move) {
            this.board = board;
            nodeList = new ArrayList<>();
            moveUsed = move;
            score = board.isPlayer1() ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        }

        /**
         * @return the move used to get there as the identifying feature.
         */
        public final String toString() {
            return moveUsed == null ? "no move" : moveUsed.toString() + ", score " + score;
        }
    }
}
