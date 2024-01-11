package byog.Core;


import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Rectangle rome, width and height must greater than 3
 */
public class Rome implements Serializable {
    private static TETile[][] world;
    private static Random rand;
    // (x1, y1) is the left-bottom point, (x2, y2) is the right-top point
    final int x1, y1, x2, y2;
    boolean connected;

    // Rome's minimum&maximum width and height
    static final int MAX_WIDTH = 20, MIN_WIDTH = 8,
            MAX_HEIGHT = 10, MIN_HEIGHT = 5;

    static void initialize(TETile[][] tiles, Random randGen) {
        Rome.world = tiles;
        Rome.rand = randGen;
    }

    public Rome(int leftBottomX, int leftBottomY, int width, int height) {
        this.x1 = leftBottomX;
        this.y1 = leftBottomY;
        if (width < 3 || height < 3) {
            throw new IllegalArgumentException(
                    "The width and height of the rome must be greater than 2.");
        }
        this.x2 = leftBottomX + width - 1;
        this.y2 = leftBottomY + height - 1;
        this.connected = false;
    }

    public boolean isConnected() {
        if (!connected) {
            connected = this.findFloorOnBorder();
        }
        return connected;
    }

    /**
     * check if two rome is overlapped.
     */
    public boolean isOverlapped(Rome other) {
        return (Math.max(this.x1, other.x1) <= (Math.min(this.x2, other.x2) + 1))
                && (Math.max(this.y1, other.y1) <= (Math.min(this.y2, other.y2) + 1));
    }

    /**
     * generate a random x position
     */
    private static int generateRandomX() {
        return RandomUtils.uniform(Rome.rand, Game.BLANK_DISTANCE,
                Game.WIDTH - MIN_WIDTH - Game.BLANK_DISTANCE);
    }

    /**
     * generate a random x position
     */
    private static int generateRandomY() {
        return RandomUtils.uniform(Rome.rand, Game.BLANK_DISTANCE,
                Game.HEIGHT - MIN_HEIGHT - Game.BLANK_DISTANCE);
    }

    /**
     * generate random rectangle rome with random position, width and height.
     */
    public static Rome generateRandomRome() {

        int x1 = Rome.generateRandomX();
        int y1 = Rome.generateRandomY();

        int maxWidthAvailable = Math.min(MAX_WIDTH, Game.WIDTH - x1 - Game.BLANK_DISTANCE);
        int maxHeightAvailable = Math.min(MAX_HEIGHT, Game.HEIGHT - y1 - Game.BLANK_DISTANCE);
        int width = RandomUtils.uniform(Rome.rand, MIN_WIDTH, maxWidthAvailable);
        int height = RandomUtils.uniform(Rome.rand, MIN_HEIGHT, maxHeightAvailable);

        return new Rome(x1, y1, width, height);
    }

    /**
     * The distance of the two romes' center
     *
     * @param other other rome
     * @return the distance of the two romes
     */
    private int distance(Rome other) {
        /* overlapped in x, return the vertical distance */
        int overLapX1 = Math.max(this.x1, other.x1);
        int overLapX2 = Math.min(this.x2, other.x2);
        if ((overLapX1 < overLapX2) && ((overLapX2 - overLapX1) >= 2)) {
            return Math.min(Math.abs(this.y1 - other.y2), Math.abs(this.y2 - other.y1));
        }

        /* overlapped in y, return the horizontal distance */
        int overLapY1 = Math.max(this.y1, other.y1);
        int overLapY2 = Math.min(this.y2, other.y2);
        if ((overLapY1 < overLapY2) && ((overLapY2 - overLapY1) >= 2)) {
            return Math.min(Math.abs(this.x1 - other.x2), Math.abs(this.x2 - other.x1));
        }

        /* not overlapped in x or y, return square of the center distance */
        int thisCenterX = (this.x1 + this.x2) / 2;
        int thisCenterY = (this.y1 + this.y2) / 2;
        int otherCenterX = (other.x1 + other.x2) / 2;
        int otherCenterY = (other.y1 + other.y2) / 2;
        return (int) (Math.pow(thisCenterX - otherCenterX, 2)
                + Math.pow(thisCenterY - otherCenterY, 2));
    }

    /**
     * check if there is floor on the border
     * if there is, it indicates that the rome is connected to other rome.
     */
    public boolean findFloorOnBorder() {
        for (int x = this.x1; x <= this.x2; x++) {
            if (Rome.world[x][this.y1] == Tileset.FLOOR
                    || Rome.world[x][this.y2] == Tileset.FLOOR) {
                return true;
            }
        }
        for (int y = this.y1; y <= this.y2; y++) {
            if (Rome.world[this.x1][y] == Tileset.FLOOR
                    || Rome.world[this.x2][y] == Tileset.FLOOR) {
                return true;
            }
        }
        return false;
    }

    /**
     * connect to other rome only overlapped in x or only overlapped in y
     *
     * @param other          other rome, must be in connectedRomes.
     * @param connectedRomes connected romes and hallways.
     * @return return true if this rome can connect to other rome with horizontal or vertical
     * hallway.
     * if success, add current rome and the hallway to the connectedRomes.
     */
    private boolean tryConnectToOtherRomeHorizontalOrVertical(Rome other,
                                                              List<Rome> connectedRomes) {
        int overLapX1 = Math.max(this.x1, other.x1);
        int overLapX2 = Math.min(this.x2, other.x2);
        /* overlapped in x, connect with vertical hallway */
        if ((overLapX1 < overLapX2) && ((overLapX2 - overLapX1) >= 2)) {
            int hallwayLeftX;
            if (overLapX2 - overLapX1 == 2) {
                hallwayLeftX = overLapX1;
            } else {
                hallwayLeftX = RandomUtils.uniform(Rome.rand, overLapX1, overLapX2 - 3 + 1);
            }
            Rome verticalHallway = new Rome(
                    hallwayLeftX,
                    Math.min(this.y2, other.y2) - 1,
                    3,
                    Math.min(Math.abs(this.y2 - other.y1), Math.abs(this.y1 - other.y2)) + 3);
            connected = true;
            connectedRomes.add(verticalHallway);
            connectedRomes.add(this);
            verticalHallway.draw();
            return true;
        }

        /* overlapped in y, connect with horizontal hallway */
        int overLapY1 = Math.max(this.y1, other.y1);
        int overLapY2 = Math.min(this.y2, other.y2);
        if ((overLapY1 < overLapY2) && ((overLapY2 - overLapY1) >= 2)) {
            int hallwayLeftY;
            if (overLapY2 - overLapY1 == 2) {
                hallwayLeftY = overLapY1;
            } else {
                hallwayLeftY = RandomUtils.uniform(Rome.rand, overLapY1, overLapY2 - 3 + 1);
            }
            Rome horizontalHallway = new Rome(
                    Math.min(this.x2, other.x2) - 1,
                    hallwayLeftY,
                    Math.min(Math.abs(this.x2 - other.x1), Math.abs(this.x1 - other.x2)) + 3,
                    3);
            connected = true;
            other.connected = true;
            connectedRomes.add(horizontalHallway);
            connectedRomes.add(this);
            horizontalHallway.draw();
            return true;
        }

        return false;
    }

    /**
     * try to connect other rome with hallway has turn
     *
     * @param other          other Rome, shouldn't overlapped in x or overlapped in y with
     *                       current rome should be in connectedRomes
     * @param connectedRomes connected romes and hallways.
     *                       <p>
     *                       add hallways and current rome to connectedRomes
     */
    private void connectToOtherRomeWithHallwayHasTurn(Rome other, List<Rome> connectedRomes) {
        int thisCenterX = (this.x1 + this.x2) / 2;
        int thisCenterY = (this.y1 + this.y2) / 2;
        int otherCenterX = (other.x1 + other.x2) / 2;
        int otherCenterY = (other.y1 + other.y2) / 2;

        int minCenterX, maxCenterX, minCenterY, maxCenterY;
        if (thisCenterX < otherCenterX) {
            minCenterX = thisCenterX;
            maxCenterX = otherCenterX;
        } else {
            minCenterX = otherCenterX;
            maxCenterX = thisCenterX;
        }
        if (thisCenterY < otherCenterY) {
            minCenterY = thisCenterY;
            maxCenterY = otherCenterY;
        } else {
            minCenterY = otherCenterY;
            maxCenterY = thisCenterY;
        }

        // the hallway turn point choose (thisCenterX, otherCenterY)
        Rome verticalHallway = new Rome(thisCenterX - 1, minCenterY - 1,
                3, maxCenterY - minCenterY + 3);
        Rome horizontalHallway = new Rome(minCenterX - 1, otherCenterY - 1,
                maxCenterX - minCenterX + 3, 3);
        connectedRomes.add(verticalHallway);
        connectedRomes.add(horizontalHallway);
        connectedRomes.add(this);
        verticalHallway.draw();
        horizontalHallway.draw();
    }

    /**
     * Generate and draw not overlapped Romes
     *
     * @return the Arraylist of the Romes
     */
    public static ArrayList<Rome> generateRomes(Map map) {
        ArrayList<Rome> romes = new ArrayList<>();
        // make sure there is at least one hallway with turn
        romes.add(Rome.generateRandomRome());
        for (int i = 0; i < 50; i++) {  // generate 50 romes at most
            boolean generateSuccess = false;
            int maxTry = 100;
            //  try to generate rome in no more than maxTry times
            for (int j = 0; j < maxTry; j++) {
                Rome newRome = Rome.generateRandomRome();
                generateSuccess = true;
                for (Rome rome : romes) {
                    if (newRome.isOverlapped(rome)) {
                        generateSuccess = false;
                        break;
                    }
                }
                if (generateSuccess) {
                    romes.add(newRome);
                    break;
                }
            }
            if (!generateSuccess) {   // not success after maxTry, no need to try anymore.
                break;
            }
        }

        for (Rome rome : romes) {
            rome.draw();
        }

        return romes;
    }

    /**
     * Connect romes in the arrayList
     */
    public static void connectRomes(List<Rome> romes) {
        List<Rome> connectedRomes = new ArrayList<>();  // will include the hallway between rooms
        List<Rome> notConnectedRomes = new ArrayList<>(romes);

        Rome firstRome = notConnectedRomes.get(0);
        notConnectedRomes.remove(0);
        firstRome.connected = true;
        connectedRomes.add(firstRome);

        for (Rome notConnectedRome : notConnectedRomes) {
            if (notConnectedRome.isConnected()) {
                // the rome is connected already by the connect operations of other romes.
                connectedRomes.add(notConnectedRome);
                continue;
            }

            // sorted the connectedRomes by the distance to the notConnectedRome
            List<Rome> sortedConnectedRomes = connectedRomes.stream()
                    .sorted(Comparator.comparingInt((Rome rome) -> rome.distance(notConnectedRome)))
                    .collect(Collectors.toList());

            // try to connect other rome with horizontal or vertical hallway
            for (Rome connectedRome : sortedConnectedRomes) {
                if (notConnectedRome.tryConnectToOtherRomeHorizontalOrVertical(connectedRome,
                        connectedRomes)) {
                    break;
                }
            }

            // if current rome can't collect to other rome with horizontal or vertical hallway
            // then connect to the nearest room with hallway has turn
            if (!notConnectedRome.isConnected()) {
                notConnectedRome.connectToOtherRomeWithHallwayHasTurn(
                        sortedConnectedRomes.get(0), connectedRomes);
            }
        }
    }


    /**
     * Draw rome in the given world
     */
    private void draw() {
        for (int x = x1; x <= x2; x++) {
            if (Rome.world[x][y1] != Tileset.FLOOR) {
                Rome.world[x][y1] = Tileset.WALL;
            }
            if (Rome.world[x][y2] != Tileset.FLOOR) {
                Rome.world[x][y2] = Tileset.WALL;
            }
        }
        for (int y = y1; y <= y2; y++) {
            if (Rome.world[x1][y] != Tileset.FLOOR) {
                Rome.world[x1][y] = Tileset.WALL;
            }
            if (Rome.world[x2][y] != Tileset.FLOOR) {
                Rome.world[x2][y] = Tileset.WALL;
            }
        }

        for (int x = x1 + 1; x < x2; x++) {
            for (int y = y1 + 1; y < y2; y++) {
                Rome.world[x][y] = Tileset.FLOOR;
            }
        }
    }
}
