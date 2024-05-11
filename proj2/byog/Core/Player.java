package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 131313131313L;
    int x;
    int y;
    int health;
    String state;
    final Map map;

    public Player(int health, Map map) {
        this.health = health;
        this.map = map;
        int[] position = map.initPlayer();
        this.x = position[0];
        this.y = position[1];
        this.state = "";
    }

    /**
     * move the player to target block
     * <p>
     * if hit the door return false else true.
     * if hit the wall, decrease the health.
     */
    private boolean move(int targetX, int targetY) {
        TETile targetBlock = map.world[targetX][targetY];
        if (targetBlock == Tileset.FLOOR) {
            // move success
            map.world[targetX][targetY] = Tileset.PLAYER;
            map.world[this.x][this.y] = Tileset.FLOOR;
            this.x = targetX;
            this.y = targetY;
            return true;
        } else if (targetBlock == Tileset.WALL) {
            state = "Hit the wall! Lose one health point!";
            health -= 1;
            return health > 0;
        } else if (targetBlock == Tileset.LOCKED_DOOR) {
            // move to the door
            map.world[targetX][targetY] = Tileset.UNLOCKED_DOOR;
            map.world[this.x][this.y] = Tileset.FLOOR;
            this.x = targetX;
            this.y = targetY;
            return false;
        } else if (targetBlock == Tileset.HEART) {
            // eat the heart
            state = "Eat one heart! health +1!";
            map.world[targetX][targetY] = Tileset.PLAYER;
            map.world[this.x][this.y] = Tileset.FLOOR;
            this.x = targetX;
            this.y = targetY;
            health += 1;
            return true;
        } else {
            throw new IllegalArgumentException("Error state.");
        }
    }

    /**
     * show player in the map
     */
    void showPlayer() {
        map.world[x][y] = Tileset.PLAYER;
//        map.showMap();
    }

    /**
     * Move the player up.
     * <p>
     * If hit the door, return false, else true
     */
    boolean moveUp() {
        // there is blank around all the rome
        state = "";
        if (move(this.x, this.y + 1)) {
            if (state.isEmpty()) {
                state = "Move up success!";
            }
            return true;
        }
        return false;
    }


    /**
     * Move the player down.
     * <p>
     * If hit the door, return false, else true
     */
    boolean moveDown() {
        // there is blank around all the rome
        state = "";
        if (move(this.x, this.y - 1)) {
            if (state.isEmpty()) {
                state = "Move down success!";
            }
            return true;
        }
        return false;
    }

    /**
     * Move the player left.
     * <p>
     * If hit the door, return false, else true
     */
    boolean moveLeft() {
        // there is blank around all the rome
        state = "";
        if (move(this.x - 1, this.y)) {
            if (state.isEmpty()) {
                state = "Move left success!";
            }
            return true;
        }
        return false;
    }

    /**
     * Move the player right.
     * <p>
     * If hit the door, return false, else true
     */
    boolean moveRight() {
        state = "";
        if (move(this.x + 1, this.y)) {
            if (state.isEmpty()) {
                state = "Move right success!";
            }
            return true;
        }
        return false;
    }

}
