package pl.tau;

import pl.tau.base.Board;
import pl.tau.base.Direction;
import pl.tau.game.Game;

public class Main {
    public static void main(String[] args) {
        Board board = Board.randomBoard(10, 10, 0.25, 12345L);
        Game game = new Game(board);

        System.out.println(board);
        System.out.println("Player position (start): " + game.getPlayerPosition());
        System.out.println("Target (stop): " + board.getStop());

        tryMove(game, Direction.RIGHT);
        tryMove(game, Direction.DOWN);
        tryMove(game, Direction.DOWN);
        tryMove(game, Direction.LEFT);
    }

    private static void tryMove(Game game, Direction direction) {
        System.out.println("Movement test: " + direction);
        boolean moved = game.move(direction);
        System.out.println("Did it work? " + moved + ", new position: " + game.getPlayerPosition());
    }
}
