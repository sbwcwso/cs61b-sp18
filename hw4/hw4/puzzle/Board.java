package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board implements WorldState {
    private static final int BLANK = 0;
    private final int size;
    private final List<Integer> immutableList;
    private final int hammingDistances;
    private final int manhattanDistances;

    /**
     * Constructs a board from an size-by-size array of tiles
     *
     * @param tiles: tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        size = tiles.length;
        List<Integer> list = new ArrayList<>();
        int hammingDistance = 0;
        int manhattanDistance = 0;
        int targetValue = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                targetValue++;
                int realValue = tiles[row][col];
                list.add(realValue);

                if (realValue == 0 || realValue == targetValue) {
                    continue;
                }

                hammingDistance += 1;
                int targetRow = (realValue - 1) / size;
                int targetCol = (realValue - 1) % size;
                manhattanDistance += Math.abs(targetRow - row) + Math.abs(targetCol - col);
            }
        }
        immutableList = Collections.unmodifiableList(list);
        hammingDistances = hammingDistance;
        manhattanDistances = manhattanDistance;
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            throw new IndexOutOfBoundsException("i, j should be in range [0, " + size + ").");
        }
        return immutableList.get(i * size + j);
    }

    /**
     * Returns the board size size
     */
    public int size() {
        return size;
    }

    /**
     * Returns the neighbors of the current board
     *
     * @source: https://joshh.ug/neighbors.html
     */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate
     */
    public int hamming() {
        return hammingDistances;
    }

    /**
     * Manhattan estimate
     */
    public int manhattan() {
        return manhattanDistances;
    }

    /**
     * Estimated distance to goal. This method simply return the results of manhattan() when
     * submitted to Gradescope.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    /**
     * Returns true if this board's tile values are the same position as y's
     */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (!(y instanceof Board)) {
            return false;
        }

        Board other = (Board) y;
        if (other.size() != this.size()) {
            return false;
        }
        for (int row = 0; row < this.size(); row++) {
            for (int col = 0; col < this.size(); col++) {
                if (this.tileAt(row, col) != other.tileAt(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the string representation of the board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
