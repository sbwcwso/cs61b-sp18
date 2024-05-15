package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Map implements Serializable {
    private static final long serialVersionUID = 121212121212L;
    transient TETile[][] world;  // may change in other class
    transient TETile[][] visibleWorld;  // may change in other class
    private int[] door;
    long seed;
    Random rand;
    List<int[]> monsters;
    List<int[]> diamonds;
    List<int[]> hearts;

    public Map(long seed) {
        this.door = new int[2];
        this.seed = seed;

        initialize();
    }

    /**
     * initialize the map
     */
    void initialize() {

        rand = new Random(seed);
        world = new TETile[Game.WIDTH][Game.HEIGHT];
        monsters = new ArrayList<>();

        Rome.initialize(world, rand);
        // Generate Map
        fillWithNothing(world);

        List<Rome> romes = Rome.generateRomes(this);
        Rome.connectRomes(romes);
        generateDoor();
        hearts = initItems(Tileset.HEART, Game.HEART_NUM);
        diamonds = initItems(Tileset.DIAMOND, Game.DIAMOND_NUM);
    }

    void removeMonsters(int[] position) {
        removeFromList(monsters, position);
    }

    void removeHeart(int[] position) {
        removeFromList(hearts, position);
    }

    void removeDiamond(int[] position) {
        removeFromList(diamonds, position);
    }

    private void removeFromList(List<int[]> list, int[] position) {
        for (Iterator<int[]> it = list.iterator(); it.hasNext(); ) {
            int[] item = it.next();
            if (Arrays.equals(item, position)) {
                it.remove();
                break;
            }
        }
    }

    int diamondNum() {
        return diamonds.size();
    }

    /**
     * Fills the given 2D array of tiles with NOTHING.
     */
    private void fillWithNothing(TETile[][] board) {
        int height = board[0].length;
        int width = board.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                board[x][y] = Tileset.NOTHING;
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
        // generate a monster near the door.
        generateMonsterAround(door[0], door[1]);
    }

    private void generateMonsterAround(int x, int y) {
        List<int[]> emptyNeighbors = getEmptyNeighbors(x, y);
        int index = RandomUtils.uniform(rand, emptyNeighbors.size());
        int[] monster = emptyNeighbors.get(index);
        int mx = monster[0];
        int my = monster[1];
        monsters.add(new int[]{mx, my});
        world[mx][my] = Tileset.MONSTER;
    }

    /**
     * init player in the map, and make sure the player is at the longest linear distance from
     * the door.
     */
    int[] initPlayer() {

        int maxDistance = 0;
        int[] position = new int[2];
        for (int x = 0; x < Game.WIDTH; x++) {
            for (int y = 0; y < Game.HEIGHT; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    int distance = (x - door[0]) * (x - door[0])
                        + (y - door[1]) * (y - door[1]);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        position[0] = x;
                        position[1] = y;
                    }
                }
            }
        }

        world[position[0]][position[1]] = Tileset.PLAYER;
        return position;
    }

    void monstersMove(Player player) {
        Iterator<int[]> iterator = monsters.iterator();
        while (iterator.hasNext()) {
            int[] monster = iterator.next();
            int x = monster[0];
            int y = monster[1];
//            if (world[x][y] != Tileset.MONSTER) {
//                iterator.remove();
//            } else {
            if ((Math.abs(player.x - x) <= 1 && player.y == y)
                || (Math.abs(player.y - y) <= 1 && player.x == x)) {
                world[x][y] = Tileset.FLOOR;
                player.state = "Attack by a monster, lose 1 health.";
                player.health--;
                iterator.remove();  // monster is dead, remove it.
            } else {
                List<int[]> emptyNeighbors = getEmptyNeighbors(x, y);
                int index = RandomUtils.uniform(rand, emptyNeighbors.size());
                int[] target = emptyNeighbors.get(index);
                int targetX = target[0];
                int targetY = target[1];
                world[targetX][targetY] = Tileset.MONSTER;
                world[x][y] = Tileset.FLOOR;
                // update the monster position
                monster[0] = targetX;
                monster[1] = targetY;
            }
//            }
        }
    }

    /**
     * init given item of given num in the map, and generate a monster around it
     * is true.
     */
    List<int[]> initItems(TETile type, int num) {
        int curNum = 0;
        List<int[]> items = new ArrayList<>();
        while (curNum < num) {
            int x = RandomUtils.uniform(rand, Game.WIDTH);
            int y = RandomUtils.uniform(rand, Game.HEIGHT);
            if (world[x][y] == Tileset.FLOOR) {
                world[x][y] = type;
                curNum++;
                items.add(new int[]{x, y});
                // add monster around it
                generateMonsterAround(x, y);
            }
        }
        return items;
    }

    private List<int[]> getEmptyNeighbors(int x, int y) {
        List<int[]> emptyNeighbors = new ArrayList<>();
        emptyNeighbors.add(new int[]{x - 1, y});
        emptyNeighbors.add(new int[]{x + 1, y});
        emptyNeighbors.add(new int[]{x, y - 1});
        emptyNeighbors.add(new int[]{x, y + 1});
        Iterator<int[]> neighborsIterator = emptyNeighbors.iterator();
        while (neighborsIterator.hasNext()) {
            int[] neighbor = neighborsIterator.next();
            int nx = neighbor[0];
            int ny = neighbor[1];
            if (world[nx][ny] != Tileset.FLOOR) {
                neighborsIterator.remove();
            }
        }
        return emptyNeighbors;
    }

    /**
     * Show the map
     */
    void showMap(Player player) {
        visibleWorld = new TETile[Game.WIDTH][Game.HEIGHT];
        fillWithNothing(visibleWorld);
        List<int[]> visibleItems = new ArrayList<>();
        visibleItems.addAll(hearts);
        visibleItems.addAll(diamonds);
//        visibleItems.addAll(monsters);
        visibleItems.add(door);

        for (int[] visibleCenter : visibleItems) {
            int x = visibleCenter[0];
            int y = visibleCenter[1];
            visibleWorld[x][y] = world[x][y];
        }

        // visible range for the player
        int cx = player.x;
        int cy = player.y;
        for (int x = cx - 2; x <= cx + 2; x++) {
            System.arraycopy(world[x], cy - 2, visibleWorld[x], cy - 2, 5);
        }

        Window.TER.renderFrame(visibleWorld);
    }

}
