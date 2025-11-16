package pl.tau.game;

import pl.tau.base.Board;
import pl.tau.base.Direction;
import pl.tau.base.Position;

public class Game {
    private final Board board;
    private Position playerPosition;

    public Game(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
        this.playerPosition = board.getStart();
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public Board getBoard() {
        return board;
    }

    public boolean canMove(Direction direction) {
        Position target = playerPosition.translate(direction);
        if (!board.isInside(target)) {
            return false;
        }
        return !board.isObstacle(target);
    }

    public boolean move(Direction direction) {
        if (!canMove(direction)) {
            return false;
        }
        playerPosition = playerPosition.translate(direction);
        return true;
    }

    public boolean isAtStop() {
        return playerPosition.equals(board.getStop());
    }
}
