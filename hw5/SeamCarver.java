import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.stream.IntStream;

public class SeamCarver {
    private Picture picture;
    private double[][] energies;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        calcEnergy();
    }

    /**
     * @return current picture
     */
    public Picture picture() {
        return new Picture(picture);
    }

    /**
     * @return width of current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * @return height of current picture
     */
    public int height() {
        return picture.height();
    }

    /**
     * @param x: column x
     * @return: 0 if x = width() - 1 else x + 1
     */
    private int xAddOne(int x) {
        if (x == width() - 1) {
            return 0;
        }
        return x + 1;
    }

    /**
     * @param x: column x
     * @return: width() - 1 if x = 0 else x - 1
     */
    private int xSubOne(int x) {
        if (x == 0) {
            return width() - 1;
        }
        return x - 1;
    }

    /**
     * @param y: row y
     * @return: 0 if y = height() - 1 else y + 1
     */
    private int yAddOne(int y) {
        if (y == height() - 1) {
            return 0;
        }
        return y + 1;
    }

    /**
     * @param y: row y
     * @return: 0 if y = height() - 1 else y + 1
     */
    private int ySubOne(int y) {
        if (y == 0) {
            return height() - 1;
        }
        return y - 1;
    }

    /**
     * calculate energy for all pixes
     */
    private void calcEnergy() {
        int width = width();
        int height = height();
        this.energies = new double[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color left = picture.get(xSubOne(x), y);
                Color right = picture.get(xAddOne(x), y);
                Color up = picture.get(x, ySubOne(y));
                Color down = picture.get(x, yAddOne(y));

                double deltaRx = left.getRed() - right.getRed();
                double deltaGx = left.getGreen() - right.getGreen();
                double deltaBx = left.getBlue() - right.getBlue();
                double deltaX2 = deltaRx * deltaRx + deltaGx * deltaGx + deltaBx * deltaBx;

                int deltaRy = up.getRed() - down.getRed();
                int deltaGy = up.getGreen() - down.getGreen();
                int deltaBy = up.getBlue() - down.getBlue();
                double deltaY2 = deltaRy * deltaRy + deltaGy * deltaGy + deltaBy * deltaBy;

                energies[y][x] = deltaX2 + deltaY2;
            }
        }
    }

    /**
     * @param x: column x
     * @param y: row y
     * @return energy of pixel at column x and row y, raise Exception if out of boundaries
     */
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) {
            throw new IllegalArgumentException("Either x or y is outside its prescribe range.");
        }
        return energies[y][x];
    }

    /**
     * get value from the given array, if out of boundries, return default
     *
     * @param array:        the array size shoule be [height()][width()]
     * @param x:            column x
     * @param y:            row y
     * @param defaultValue: default value
     * @return the values of array[y][x], if out of boundries return defalut
     */
    private double getValueFromArray(double[][] array, int x, int y, double defaultValue) {
        if (x < 0 || y < 0 || x > array[0].length - 1 || y > array.length - 1) {
            return defaultValue;
        }
        return array[y][x];
    }

    /**
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        double[][] reverseEnergies = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                reverseEnergies[i][j] = energies[j][i];
            }
        }
        return findSeamHelper(reverseEnergies);
    }

    /**
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        return findSeamHelper(energies);
    }

    /**
     * @param energiesArray
     * @return the vertical seam path of the given energies array
     */
    private int[] findSeamHelper(double[][] energiesArray) {
        int height = energiesArray.length;
        int width = energiesArray[0].length;
        // M(i, j) - cost of minimum cost path ending at (i, j)
        double[][] M = new double[height][width];

        IntStream.range(0, width).forEach(x -> M[0][x] = energiesArray[0][x]);
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double top = getValueFromArray(M, x, y - 1, Double.MAX_VALUE);
                double topLeft = getValueFromArray(M, x - 1, y - 1, Double.MAX_VALUE);
                double topRight = getValueFromArray(M, x + 1, y - 1, Double.MAX_VALUE);
                M[y][x] = energiesArray[y][x] + Math.min(top, Math.min(topLeft, topRight));
            }
        }

        return constructSeam(M);
    }

    /**
     * construct seam path from the given M
     *
     * @param M: M(i, j) - cost of minimum cost path ending at (i, j)
     * @return: seam path
     */
    private int[] constructSeam(double[][] M) {
        int height = M.length;
        int width = M[0].length;

        int lastRow = height - 1;
        int endX = 0;
        double minValue = Double.MAX_VALUE;
        for (int x = 0; x < width; x++) {
            if (M[lastRow][x] < minValue) {
                minValue = M[lastRow][x];
                endX = x;
            }
        }

        int[] seam = new int[height];
        seam[lastRow] = endX;

        for (int y = lastRow - 1; y >= 0; y--) {
            int x = seam[y + 1];
            double top = getValueFromArray(M, x, y, Double.MAX_VALUE);
            double topLeft = getValueFromArray(M, x - 1, y, Double.MAX_VALUE);
            double topRight = getValueFromArray(M, x + 1, y, Double.MAX_VALUE);
            if (top <= topLeft && top <= topRight) {
                seam[y] = x;
            } else if (topLeft <= top && topLeft <= topRight) {
                seam[y] = x - 1;
            } else {
                seam[y] = x + 1;
            }
        }
        return seam;
    }

    /**
     * remove horizontal seam from picture
     *
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        picture = new Picture(SeamRemover.removeHorizontalSeam(picture, seam));
        calcEnergy();
    }

    /**
     * remove vertical seam from picture
     *
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        picture = new Picture(SeamRemover.removeVerticalSeam(picture, seam));
        calcEnergy();
    }
}
