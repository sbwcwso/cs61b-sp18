package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER = new TETile('@', Color.white, Color.black, "player");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
        "wall: if you hit the wall, you will lose 1 health.");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
        "floor: you can walk through floor.");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
        "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
        "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile HEART = new TETile('♥', Color.red, Color.black, "Heart"
        + ": you will gain 1 health if you eat a heart.");
    public static final TETile DIAMOND = new TETile('◆', Color.yellow, Color.black, "Diamond"
        + ": you need collect all the diamond to win the game.");
    public static final TETile MONSTER = new TETile('☠', Color.black, Color.red, "Monster"
        + ": they will attack you when you get close to them, you can also attack them. "
        + "you will lose 1 point at both condition.");
}


