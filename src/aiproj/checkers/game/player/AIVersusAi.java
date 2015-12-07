package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.Config;
import aiproj.checkers.graphics.StartScreen;

/**
 * Created by mknutsen on 12/6/15.
 */
public class AIVersusAi {
    
    private static StartScreen.Heuristic player1Heuristic = StartScreen.Heuristic.A;
    
    private static StartScreen.Heuristic player2Hueristic = StartScreen.Heuristic.B;
    
    public static void main(String[] args) {
        Config.DEBUG = false;
        System.out.println(playGame());
    }
    
    public static int playGame() {
        CheckersBoard board = new CheckersBoard();
        board.newGame(null);
        System.out.println("making game");
        AIPlayer player1 = new AIPlayer(player1Heuristic), player2 = new AIPlayer(player2Hueristic);
        System.out.println("player1");
        player1.setGame(board, true);
        
        while (player1.isThinking()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("player2");
        player2.setGame(board, false);
        while (player2.isThinking()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("turn time");
        while (board.checkWin() == 0) {
            String gameString = board.getBoardString();
            if (gameString != null && gameString.indexOf("\n") > 0) {
                while (gameString.indexOf("\n") != gameString.lastIndexOf("\n")) {
                    gameString = gameString.substring(gameString.indexOf("\n") + 1);
                }
                System.out.println("Move: "+gameString);
                System.out.println(board);
            }
            if (board.isPlayer1()) {
                player1.makeMove(player1.triggerTurn());
            } else {
                player2.makeMove(player2.triggerTurn());
            }
        }
        return board.checkWin();
    }
}
