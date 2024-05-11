package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map implements Serializable {
    private static final long serialVersionUID = 121212121212L;
    transient TETile[][] world;  // may change in other class
    private final int[] door;
    Random rand;
    private final long seed;

    public Map(long seed) {
        this.door = new int[2];  // TODO delete this
        this.seed = seed;
        initialize();
    }

    /**
     * initialize the map
     */
    void initialize() {
        rand = new Random(seed);

        world = new TETile[Game.WIDTH][Game.HEIGHT];

        Rome.initialize(world, rand);
        // Generate Map
        fillWithNothing();

        List<Rome> romes = Rome.generateRomes(this);
        Rome.connectRomes(romes);
        generateDoor();
        initItems(Tileset.HEART, 3);
        initItems(Tileset.Gold, 3);
    }

    /**
     * Fills the given 2D array of tiles with NOTHING.
     */
    private void fillWithNothing() {
        int height = world[0].length;
        int width = world.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    /**
     * Find a door of the given tiles
     */
    private void generateDoor() {

        // Random select the x position of the door
        int[] doorXs = new int[Game.WIDTH];
        for (int i = 0; i < Game.WIDTH; i++) {
            doorXs[i] = i;
        }
        RandomUtils.shuffle(rand, doorXs);

        // Select the position y of the door
        doorXLoop:
        for (int doorX : doorXs) {
            for (int doorY = 0; doorY < Game.HEIGHT - 1; doorY++) {
                if (world[doorX][doorY] == Tileset.WALL) {
                    if (world[doorX][doorY + 1] == Tileset.FLOOR) {
                        world[doorX][doorY] = Tileset.LOCKED_DOOR;
                        door[0] = doorX;
                        door[1] = doorY;
                        break doorXLoop;
                    }
                    break;
                }
            }
        }
    }

    /**
     * init player in the map
     * find the longest distance from the door in the floors
     */
    int[] initPlayer() {

        int minimumDistance = RandomUtils.uniform(rand, 30, 40);
        int[] position = new int[2];
        List<int[]> players = new ArrayList<>();
        for (int x = 0; x < Game.WIDTH; x++) {
            for (int y = 0; y < Game.HEIGHT; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    int distance = (x - door[0]) * (x - door[0])
                        + (y - door[1]) * (y - door[1]);
                    if (distance > minimumDistance) {
                        players.add(new int[]{x, y});
                    }
                }
            }
        }


        int randomIndex = RandomUtils.uniform(rand, players.size());
        position[0] = players.get(randomIndex)[0];
        position[1] = players.get(randomIndex)[1];
        world[position[0]][position[1]] = Tileset.PLAYER;

        return position;
    }

    /**
     * init given item of given num in the map
     */
    void initItems(TETile type, int num) {
        int curHeartNum = 0;
        while (curHeartNum < num) {
            int x = RandomUtils.uniform(rand, Game.WIDTH);
            int y = RandomUtils.uniform(rand, Game.HEIGHT);
            if (world[x][y] == Tileset.FLOOR) {
                world[x][y] = type;
                curHeartNum++;
            }
        }
    }

    /**
     * Show the map
     */
    void showMap() {
        Window.TER.renderFrame(world);
    }

}
