package pl.tau.base;

import java.util.Random;

public class Board {

    private final int rows;
    private final int cols;
    private final CellType[][] grid;
    private final Position start;
    private final Position stop;

    public Board(int rows, int cols, CellType[][] grid, Position start, Position stop) {
        if (rows < 5 || cols < 5) {
            throw new IllegalArgumentException("Board must be at least 5x5");
        }
        if (grid == null) {
            throw new IllegalArgumentException("Grid cannot be null");
        }
        if (grid.length != rows || grid[0].length != cols) {
            throw new IllegalArgumentException("Grid size does not match rows/cols");
        }
        if (start == null || stop == null) {
            throw new IllegalArgumentException("Start and stop cannot be null");
        }
        if (start.equals(stop)) {
            throw new IllegalArgumentException("Start and stop must be different");
        }

        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.start = start;
        this.stop = stop;

        if (start.isOrthogonallyAdjacent(stop)) {
            throw new IllegalArgumentException("Start and stop cannot be orthogonally adjacent");
        }
        if (!isOnEdge(start) || !isOnEdge(stop)) {
            throw new IllegalArgumentException("Start and stop must be on the edge");
        }
    }

    public static Board randomBoard(int rows, int cols, double obstacleProbability, long seed) {
        if (rows < 5 || cols < 5) {
            throw new IllegalArgumentException("Board must be at least 5x5");
        }
        if (obstacleProbability < 0.0 || obstacleProbability > 1.0) {
            throw new IllegalArgumentException("Obstacle probability must be between 0 and 1");
        }

        Random random = new Random(seed);
        CellType[][] grid = new CellType[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        Position start;
        Position stop;

        do {
            start = randomEdgePosition(rows, cols, random);
            stop = randomEdgePosition(rows, cols, random);
        } while (start.equals(stop) || start.isOrthogonallyAdjacent(stop));

        grid[start.row()][start.col()] = CellType.START;
        grid[stop.row()][stop.col()] = CellType.STOP;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position p = new Position(r, c);
                if (p.equals(start) || p.equals(stop)) {
                    continue;
                }
                if (random.nextDouble() < obstacleProbability) {
                    grid[r][c] = CellType.OBSTACLE;
                }
            }
        }

        return new Board(rows, cols, grid, start, stop);
    }

    private static Position randomEdgePosition(int rows, int cols, Random random) {
        int edge = random.nextInt(4);
        return switch (edge) {
            case 0 -> new Position(0, random.nextInt(cols));
            case 1 -> new Position(rows - 1, random.nextInt(cols));
            case 2 -> new Position(random.nextInt(rows), 0);
            case 3 -> new Position(random.nextInt(rows), cols - 1);
            default -> throw new IllegalStateException("Unexpected value: " + edge);
        };
    }

    public boolean isInside(Position position) {
        int r = position.row();
        int c = position.col();
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public boolean isObstacle(Position position) {
        if (!isInside(position)) {
            return true;
        }
        return grid[position.row()][position.col()] == CellType.OBSTACLE;
    }

    public boolean isOnEdge(Position position) {
        int r = position.row();
        int c = position.col();
        return r == 0 || r == rows - 1 || c == 0 || c == cols - 1;
    }

    public CellType getCellType(Position position) {
        if (!isInside(position)) {
            throw new IllegalArgumentException("Position out of board: " + position);
        }
        return grid[position.row()][position.col()];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Position getStart() {
        return start;
    }

    public Position getStop() {
        return stop;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board ").append(rows).append("x").append(cols)
                .append(" start=").append(start)
                .append(" stop=").append(stop)
                .append(System.lineSeparator());

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellType type = grid[r][c];
                char ch;
                if (r == start.row() && c == start.col()) {
                    ch = 'A';
                } else if (r == stop.row() && c == stop.col()) {
                    ch = 'B';
                } else {
                    ch = switch (type) {
                        case EMPTY -> '.';
                        case OBSTACLE -> 'X';
                        case START -> 'A';
                        case STOP -> 'B';
                    };
                }
                sb.append(ch).append(' ');
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
