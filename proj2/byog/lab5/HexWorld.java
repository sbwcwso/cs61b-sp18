package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final Random RANDOM = new Random();

    private static class Position {
        int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Computes the width of row i for a size s hexagon.
     *
     * @param s The size of the hex.
     * @param i The row number where i = 0 is the bottom row.
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }

        return s + 2 * effectiveI;
    }

    /**
     * Computes relative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     * xxxx
     * xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     * xxxxxx
     * xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return -effectiveI;
    }

    /**
     * Adds a row of the same tile.
     *
     * @param world the world to draw on
     * @param p     the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t     the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    /**
     * Adds a hexagon to the world.
     *
     * @param world the world to draw on
     * @param p     the bottom left coordinate of the hexagon
     * @param s     the size of the hexagon
     * @param t     the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;

            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);

        }
    }


    /**
     * Fills the given 2D array of tiles with NOTHING.
     */
    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Picks a RANDOM tile with a 33% change of being
     * a wall, 33% chance of being a flower, and 33%
     * chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(10);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOOR;
            case 2:
                return Tileset.GRASS;
            case 3:
                return Tileset.WATER;
            case 4:
                return Tileset.FLOWER;
            case 5:
                return Tileset.LOCKED_DOOR;
            case 6:
                return Tileset.UNLOCKED_DOOR;
            case 7:
                return Tileset.SAND;
            case 8:
                return Tileset.MOUNTAIN;
            case 9:
                return Tileset.TREE;
            default:
                return Tileset.NOTHING;
        }
    }

    /**
     * Draw n random vertical hexes from bottom
     *
     * @param world the world
     * @param size  the size of the Hexes
     * @param n     the number of Vertical Hexes
     * @param p     the bottom left position of the bottom Hex
     */
    public static void drawRandomVerticalHexes(TETile[][] world, int size, int n, Position p) {
        Position leftBottomPosition = new Position(p.x, p.y);
        for (int i = 0; i < n; i++) {
            addHexagon(world, leftBottomPosition, size, randomTile());
            leftBottomPosition.y += 2 * size;
        }
    }

    public static void main(String[] args) {
        final int size = 7;
        final int height = 2 * size * 5;
        final int width = 3 * (size + 2 * (size - 1)) + 2 * size;


        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] world = new TETile[width][height];
        fillWithNothing(world);

        Position startPosition = new Position(width / 2 - size / 2, 0);
        drawRandomVerticalHexes(world, size, 5, startPosition);

        Position leftSidePosition = new Position(startPosition.x - 2 * size + 1, startPosition.y + size);
        drawRandomVerticalHexes(world, size, 4, leftSidePosition);

        leftSidePosition.x -= 2 * size - 1;
        leftSidePosition.y += size;
        drawRandomVerticalHexes(world, size, 3, leftSidePosition);

        Position rightSidePosition = new Position(startPosition.x + 2 * size - 1, startPosition.y + size);
        drawRandomVerticalHexes(world, size, 4, rightSidePosition);

        rightSidePosition.x += 2 * size - 1;
        rightSidePosition.y += size;
        drawRandomVerticalHexes(world, size, 3, rightSidePosition);

        ter.renderFrame(world);
    }

    @Test
    public void testHexRowWidth() {
        assertEquals(3, hexRowWidth(3, 5));
        assertEquals(5, hexRowWidth(3, 4));
        assertEquals(7, hexRowWidth(3, 3));
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(5, hexRowWidth(3, 1));
        assertEquals(3, hexRowWidth(3, 0));
        assertEquals(2, hexRowWidth(2, 0));
        assertEquals(4, hexRowWidth(2, 1));
        assertEquals(4, hexRowWidth(2, 2));
        assertEquals(2, hexRowWidth(2, 3));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }
}
