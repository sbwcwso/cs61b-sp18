package byog.Core;

import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;


/**
 * The Game window
 */
public class Window {

    static final Font BIG_FONT = new Font("Monaco", Font.BOLD, 60);
    static final Font MIDDLE_FONT = new Font("Monaco", Font.BOLD, 30);
    static final Font SMALL_FONT = new Font("Monaco", Font.BOLD, 20);
    static final TERenderer TER = new TERenderer();

    /**
     * Show the main menu
     */
    static void mainMenu() {
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.setFont(BIG_FONT);
        StdDraw.text(40, 50, "CS61B: THE GAME");

        StdDraw.setFont(MIDDLE_FONT);
        StdDraw.text(40, 40, "New Game (N)");
        StdDraw.text(40, 37, "Load Game (L)");
        StdDraw.text(40, 34, "Quit (Q)");

        StdDraw.show();
    }

    /**
     * show the end menu
     */
    static void endMenu(Player player) {
        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.filledRectangle(Game.WIDTH / 2.0, Game.HEIGHT - 1, Game.WIDTH / 2.0, 1);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(Game.WIDTH / 2.0, Game.HEIGHT / 2.0, 32, 8);

        StdDraw.setFont(BIG_FONT);
        StdDraw.setPenColor(StdDraw.BLUE);

        String text;
        if (player.health > 0) {
            text = "😊YOU Win😊";
        } else {
            text = "😢You Lose😢";
        }
        StdDraw.text(Game.WIDTH / 2.0, Game.HEIGHT / 2.0, text);

        StdDraw.show();
    }

    /**
     * game save menu
     */
    static void saveMenu() {

        StdDraw.setPenColor(StdDraw.BLACK);

        StdDraw.filledRectangle(Game.WIDTH / 2.0, Game.HEIGHT - 1, Game.WIDTH / 2.0, 1);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(Game.WIDTH / 2.0, Game.HEIGHT / 2.0, 32, 8);

        StdDraw.setFont(BIG_FONT);
        StdDraw.setPenColor(StdDraw.BLUE);

        StdDraw.text(Game.WIDTH / 2.0, Game.HEIGHT / 2.0 + 2, "Your Game Has Been Saved.");
        StdDraw.text(Game.WIDTH / 2.0, Game.HEIGHT / 2.0 - 2, "Game Over.");

        StdDraw.show();
        StdDraw.pause(2000);
    }

    /**
     * Seed input menu
     */
    static void seedInput(long seed) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(MIDDLE_FONT);
        StdDraw.text(40, 40, "Please input a random seed😊");
        StdDraw.text(40, 37, "😊Press S when your finished.");
        StdDraw.text(40, 34, "⇒ " + String.valueOf(seed));

        StdDraw.show();

    }

    /**
     * update game head up display
     */
    static void updateHUD(Player player) {
        int x = Math.min((int) StdDraw.mouseX(), Game.WIDTH - 1);
        int y = Math.min((int) StdDraw.mouseY(), Game.HEIGHT - 1);


        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(Game.WIDTH / 2.0, Game.HEIGHT - 1, Game.WIDTH / 2.0, 1);

        String leftText = x + ", " + y + ": " + player.map.world[x][y].description();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(2, Game.HEIGHT - 1, leftText);

        StringBuilder rightText = new StringBuilder();
        if (Game.DIAMOND_NUM > 0) {
            rightText.append(Game.DIAMOND_NUM).append(" diamonds need to be "
                + "collected.         ");
        } else {
            rightText.append("You have collected all the diamonds, go to the door to win the "
                + "game!          ");
        }

        int health = player.health;
        rightText.append("Health: ");
        for (int i = 0; i < health; i++) {
            rightText.append("❤");
        }

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.textRight(Game.WIDTH - 2, Game.HEIGHT - 1, rightText.toString());

        String middleText = player.state;
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(Game.WIDTH / 2.0 - 10, Game.HEIGHT - 1, middleText);

        StdDraw.show();
    }
}
