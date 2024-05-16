package byog.Core;

import byog.TileEngine.TETile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Game {
    //    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    public static final int BLANK_DISTANCE = 3;
    static int DIAMOND_NUM = 5;
    static int HEART_NUM = 3;

    Player player;

    private Random rand = new Random();


    /**
     * new game
     */
    private void newGame() {
        long seed = Keyboard.getRandomSeed();
        player = new Player(3, new Map(seed));
    }


    /**
     * play the game
     */
    private void playGame() {
        char ch;
        do {
            player.map.showMap(player);
            ch = Keyboard.getCharAndUpdateHUD(player);
            if (ch == ':') {
                ch = Keyboard.getCharAndUpdateHUD(player);
                if (ch == 'Q' || ch == 'q') {
                    saveGame();
                    Window.saveMenu();
                    return;
                } else {
                    throw new IllegalArgumentException("Illegal input.");
                }
            }

            if (!movePlayer(ch)) {
                break;
            } else {
                Window.updateHUD(player);
                player.map.showMap(player);
                player.map.monstersMove(player);
                Window.updateHUD(player);
                if (player.health <= 0) {
                    break;
                }
            }
        } while (true);
        player.map.showMap(player);
        Window.endMenu(player);
    }

    /**
     * Move the player with W, A, S, D
     */
    private boolean movePlayer(char direction) {
        switch (direction) {
            case 'W':
            case 'w':
                return player.moveUp();
            case 'A':
            case 'a':
                return player.moveLeft();
            case 'S':
            case 's':
                return player.moveDown();
            case 'D':
            case 'd':
                return player.moveRight();
            default:
                return true;
        }
    }

    private void saveGame() {
        try {
            FileOutputStream fs = new FileOutputStream("world.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(player);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void loadGame() {
        File f = new File("world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                player = (Player) os.readObject();
                player.map.reloadInitialize();
                player.showPlayer();
                os.close();
                return;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no player has been saved yet, we return a new one. */
        player = new Player(5, new Map(rand.nextLong()));
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public TETile[][] playWithKeyboard() {

        // show the main menu
        Window.TER.initialize(WIDTH, HEIGHT);
        Window.mainMenu();

        // get the input
        // N(New Game) L(Load Game) Q(Quit)
        switch (Keyboard.getChar()) {
            case 'N':
            case 'n':
                newGame();
                break;
            case 'L':
            case 'l':
                loadGame();
                break;
            case 'Q':
            case 'q':
                System.exit(0);
            default:
                throw new IllegalArgumentException("Illegal input.");
        }

        Window.TER.initialize(WIDTH, HEIGHT);
        playGame();
        return player.map.world;
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        char beginChar = input.charAt(0);
        int index = 0;
        if (beginChar == 'N' || beginChar == 'n') {
            int seedEndIndex = input.indexOf('S');
            if (seedEndIndex == -1) {
                seedEndIndex = input.indexOf('s');
            }
            long seed = Long.parseLong(input.substring(1, seedEndIndex));
            player = new Player(5, new Map(seed));

            index = seedEndIndex + 1;
        } else if (beginChar == 'L' || beginChar == 'l') {
            loadGame();
            index = 1;
        } else {
            throw new IllegalArgumentException("Illegal input.");
        }

        while (index < input.length()) {
            char ch = input.charAt(index);
            if (ch == ':') {
                ch = input.charAt(index + 1);
                if (ch == 'Q' || ch == 'q') {
                    saveGame();
                    break;
                } else {
                    throw new IllegalArgumentException("Illegal input.");
                }
            }
            movePlayer(ch);

            index += 1;
        }

        return player.map.world;
    }
}
