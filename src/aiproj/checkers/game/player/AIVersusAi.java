package aiproj.checkers.game.player;

import aiproj.checkers.game.CheckersBoard;
import aiproj.checkers.graphics.Config;
import aiproj.checkers.graphics.StartScreen;

/**
 * AI versus AI runs two AIs against eachother using whichever heuristic is specified at the top of the file
 */
public class AIVersusAi {
    
    private static StartScreen.Heuristic player1Heuristic = StartScreen.Heuristic.B;

    private static StartScreen.Heuristic player2Hueristic = StartScreen.Heuristic.A;
    
    public static void main(String[] args) {
        Config.DEBUG = false;
        System.out.println(playGame());
    }

    /**
     * Runs two AIs against each other<br> <b>Multiple known issues:</b><br> <ol><li>There is some threading issue that
     * causes the players not to finish being created. If it runs for a minute and hasn't printed any moves, quit it and
     * run it again.</li><li>If the pieces end up far away from eachother, a real player would forfit. The AIs just run
     * away from eachother. If you notice its just printing out the same number over and over again, call it quits.
     * </li><li>If the player loses by not having any moves to make, it will throw a null pointer exception. Ignore
     * that.</li></ol>
     *
     * @return who won
     */
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
                System.out.println("Move: " + gameString);
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
