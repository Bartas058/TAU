package pl.tau.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.tau.base.Board;
import pl.tau.base.CellType;
import pl.tau.base.Direction;
import pl.tau.base.Position;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Board board;
    private Game game;

    @BeforeEach
    void setup() {
        int rows = 5;
        int cols = 5;
        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start = new Position(2, 0);
        Position stop = new Position(2, 4);
        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        grid[2][2] = CellType.OBSTACLE;

        board = new Board(rows, cols, grid, start, stop);
        game = new Game(board);
    }

    @Test
    void playerStartsAtStartPosition() {
        assertEquals(board.getStart(), game.getPlayerPosition());
    }

    @Test
    void canMoveToEmptyCell() {
        assertTrue(game.canMove(Direction.RIGHT));
        boolean moved = game.move(Direction.RIGHT);
        assertTrue(moved);
        assertEquals(new Position(2, 1), game.getPlayerPosition());
    }

    @Test
    void cannotMoveIntoObstacle() {
        assertTrue(game.move(Direction.RIGHT));
        assertFalse(game.canMove(Direction.RIGHT));
        boolean moved = game.move(Direction.RIGHT);
        assertFalse(moved);
        assertEquals(new Position(2, 1), game.getPlayerPosition());
    }

    @Test
    void cannotMoveOutsideBoard() {
        assertFalse(game.canMove(Direction.LEFT));
        boolean moved = game.move(Direction.LEFT);
        assertFalse(moved);
        assertEquals(new Position(2, 0), game.getPlayerPosition());
    }

    @Test
    void canReachStopIfPathIsFree() {
        assertTrue(game.move(Direction.RIGHT));
        assertTrue(game.move(Direction.DOWN));
        assertTrue(game.move(Direction.RIGHT));
        assertTrue(game.move(Direction.RIGHT));
        assertTrue(game.move(Direction.UP));
        assertTrue(game.move(Direction.RIGHT));

        assertTrue(game.isAtStop());
        assertEquals(board.getStop(), game.getPlayerPosition());
    }

    @Test
    void isAtStopReturnsTrueWhenPlayerReachesEndOnSimpleBoard() {
        int rows = 5;
        int cols = 5;

        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start = new Position(0, 0);
        Position stop = new Position(0, 4);

        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        Board simpleBoard = new Board(rows, cols, grid, start, stop);
        Game simpleGame = new Game(simpleBoard);

        assertFalse(simpleGame.isAtStop());

        assertTrue(simpleGame.move(Direction.RIGHT));
        assertTrue(simpleGame.move(Direction.RIGHT));
        assertTrue(simpleGame.move(Direction.RIGHT));
        assertTrue(simpleGame.move(Direction.RIGHT));

        assertEquals(stop, simpleGame.getPlayerPosition());
        assertTrue(simpleGame.isAtStop());
    }

    @Test
    void getBoardReturnsTheSameBoardUsedToCreateGame() {
        Board originalBoard = this.board;
        Game game = new Game(originalBoard);

        Board returnedBoard = game.getBoard();

        assertNotNull(returnedBoard);
        assertSame(originalBoard, returnedBoard);
    }
}
