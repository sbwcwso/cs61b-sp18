package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Assert;
import org.junit.Test;

public class TestCore {
    @Test
    public void testPlayWithInputString() {
        Game game1 = new Game();
        Game game2 = new Game();
        String input = "n5197880843569031643s";
        TETile[][] res1 = game1.playWithInputString(input);
        TETile[][] res2 = game2.playWithInputString(input);
        Assert.assertArrayEquals(res1, res2);
    }
}
