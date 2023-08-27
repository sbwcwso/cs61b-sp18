package byog.Core;


import edu.princeton.cs.introcs.StdDraw;

/**
 * Handle the keyboard input
 */
public class Keyboard {

    /**
     * Get a char from input
     */
    static char getChar() {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(10);
        }
        return StdDraw.nextKeyTyped();
    }


    /**
     * Get a char from input and update HUD
     */
    static char getCharAndUpdateHUD(Player player) {

        Window.updateHUD(player);
        do {
            Window.updateHUD(player);
            StdDraw.pause(10);
        } while (!StdDraw.hasNextKeyTyped());

        return StdDraw.nextKeyTyped();
    }

    /**
     * Get random seed from input.
     */
    static long getRandomSeed() {
        long seed = 0;
        Window.seedInput(seed);
        while (true) {
            char input = Keyboard.getChar();
            if (input == 'S' || input == 's') {
                break;
            }
            seed = seed * 10 + input - '0';
            Window.seedInput(seed);
        }
        return seed;
    }

}
