package pl.tau.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void randomBoardHasStartAndStopOnEdgeAndNotAdjacent() {
        Board board = Board.randomBoard(8, 9, 0.2, 42L);

        Position start = board.getStart();
        Position stop = board.getStop();

        assertNotNull(start);
        assertNotNull(stop);
        assertNotEquals(start, stop, "Start and stop should be different");

        assertTrue(board.isOnEdge(start), "Start should be on board edge");
        assertTrue(board.isOnEdge(stop), "Stop should be on board edge");

        assertFalse(start.isOrthogonallyAdjacent(stop),
                "Start and stop should not be orthogonally adjacent");
    }

    @Test
    void boardSmallerThan5x5IsRejected() {
        IllegalArgumentException ex1 = assertThrows(
                IllegalArgumentException.class,
                () -> Board.randomBoard(4, 5, 0.2, 1L)
        );
        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> Board.randomBoard(5, 4, 0.2, 1L)
        );

        assertTrue(ex1.getMessage().contains("5x5"));
        assertTrue(ex2.getMessage().contains("5x5"));
    }

    @Test
    void obstacleProbabilityOutOfRangeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> Board.randomBoard(5, 5, -0.1, 1L));
        assertThrows(IllegalArgumentException.class,
                () -> Board.randomBoard(5, 5, 1.1, 1L));
    }

    @Test
    void boardRejectsStartNotOnEdge() {
        int rows = 5;
        int cols = 5;

        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start = new Position(2, 2);
        Position stop = new Position(0, 4);

        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        assertThrows(IllegalArgumentException.class,
                () -> new Board(rows, cols, grid, start, stop));
    }

    @Test
    void boardRejectsAdjacentStartAndStop() {
        int rows = 5;
        int cols = 5;

        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start = new Position(0, 0);
        Position stop = new Position(0, 1);

        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        assertThrows(IllegalArgumentException.class,
                () -> new Board(rows, cols, grid, start, stop));
    }

    @Test
    void gettersReturnCorrectBoardDimensionsAndCellTypes() {
        int rows = 6;
        int cols = 7;

        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start = new Position(0, 0);
        Position stop = new Position(5, 6);

        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        Board board = new Board(rows, cols, grid, start, stop);

        assertEquals(6, board.getRows());
        assertEquals(7, board.getCols());

        assertEquals(CellType.START, board.getCellType(start));
        assertEquals(CellType.STOP, board.getCellType(stop));
        assertEquals(CellType.EMPTY, board.getCellType(new Position(1, 1)));
    }
}
