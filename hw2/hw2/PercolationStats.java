package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] percolationThresholds;

    /**
     * perform independent trials on an n-by-n grid
     */
    public PercolationStats(int n, int trials, PercolationFactory pf) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials should be greater than 0");
        }
        percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = pf.make(n);
            while (!percolation.percolates()) {
                int row, col;
                row = StdRandom.uniform(0, n);
                col = StdRandom.uniform(0, n);
                percolation.open(row, col);
            }
            percolationThresholds[i] = ((double) percolation.numberOfOpenSites()) / (n * n);
        }
    }

    /**
     * sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    /**
     * low endpoint of 95% confidence interval
     */
    public double confidenceLow() {
        return mean() - CONFIDENCE_95 * stddev() / (Math.sqrt(percolationThresholds.length));
    }

    /**
     * high endpoint of 95% confidence interval
     */
    public double confidenceHigh() {
        return mean() + CONFIDENCE_95 * stddev() / (Math.sqrt(percolationThresholds.length));
    }

    /**
     * test client (see below)
     */
    // public static void main(String[] args) {
    //     int n = Integer.parseInt(args[0]);
    //     int trails = Integer.parseInt(args[1]);
    //
    //     PercolationStats percolationStat = new PercolationStats(n, trails,
    //             new PercolationFactory());
    //     StdOut.println("mean                    = " + percolationStat.mean());
    //     StdOut.println("stddev                  = " + percolationStat.stddev());
    //     StdOut.println("95% confidence interval = [" + percolationStat.confidenceLow()
    //             + ", " + percolationStat.confidenceHigh() + "]");
    // }
}
