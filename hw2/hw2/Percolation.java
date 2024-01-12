package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int gridSize;
    private final boolean[][] openStates;
    private final WeightedQuickUnionUF uF;
    private final WeightedQuickUnionUF uFForFull;
    private final int top;
    private final int bottom;
    private boolean percolateFlag;
    private int openStatesNum;

    /**
     * creates n-by-n grid, with all sites initially blocked
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size n should be greater than 0");
        }
        gridSize = n;
        top = 0;
        bottom = n * n + 1;
        openStates = new boolean[n][n];
        uF = new WeightedQuickUnionUF(n * n + 2);
        uFForFull = new WeightedQuickUnionUF(n * n + 1);
        percolateFlag = false;
        openStatesNum = 0;
    }

    private int uFIndex(int row, int col) {
        return row * gridSize + col + 1;
    }

    /**
     * opens the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col should be in range [0, gridSize-1]");
        }

        if (isOpen(row, col)) {
            return;
        }
        openStatesNum += 1;

        openStates[row][col] = true;
        int curIndex = uFIndex(row, col);

        // check the top site
        if (row == 0) {
            uF.union(curIndex, top);
            uFForFull.union(curIndex, top);
        } else if (isOpen(row - 1, col)) {
            uF.union(curIndex, uFIndex(row - 1, col));
            uFForFull.union(curIndex, uFIndex(row - 1, col));
        }

        // check the bottom site
        if (row == (gridSize - 1)) {
            uF.union(curIndex, bottom);
        } else if (isOpen(row + 1, col)) {
            uF.union(uFIndex(row + 1, col), curIndex);
            uFForFull.union(uFIndex(row + 1, col), curIndex);
        }

        // check the left site
        if (col != 0 && isOpen(row, col - 1)) {
            uF.union(uFIndex(row, col - 1), curIndex);
            uFForFull.union(uFIndex(row, col - 1), curIndex);
        }
        if (col != (gridSize - 1) && isOpen(row, col + 1)) {
            uF.union(uFIndex(row, col + 1), curIndex);
            uFForFull.union(uFIndex(row, col + 1), curIndex);
        }
    }

    /**
     * is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col should be in range [0, gridSize-1]");
        }
        return openStates[row][col];
    }

    /**
     * is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IndexOutOfBoundsException("row and col should be in range [0, gridSize-1]");
        }
        return uFForFull.find(uFIndex(row, col)) == uFForFull.find(top);
    }

    /**
     * returns the number of open sites
     */
    public int numberOfOpenSites() {
        return openStatesNum;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        if (percolateFlag) {
            return true;
        }
        if (uF.find(top) == uF.find(bottom)) {
            percolateFlag = true;
        }
        return percolateFlag;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        percolation.open(0, 0);
        percolation.open(0, 1);
        percolation.open(0, 3);
        percolation.open(1, 3);
        percolation.open(2, 3);
        percolation.open(2, 4);
        percolation.open(3, 0);
        percolation.open(4, 0);
        percolation.open(4, 1);
        percolation.open(4, 3);
        percolation.open(4, 4);

        assert percolation.isOpen(2, 3);
        assert !percolation.isOpen(3, 3);
        assert percolation.isFull(2, 4);
        assert !percolation.isFull(4, 1);
        assert percolation.numberOfOpenSites() == 11;
        assert !percolation.percolates();
        percolation.open(3, 3);
        assert percolation.percolates();
        System.out.println("Success!");
    }
}
