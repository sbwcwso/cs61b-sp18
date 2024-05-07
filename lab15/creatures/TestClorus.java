package creatures;

import huglife.Action;
import huglife.Direction;
import huglife.Empty;
import huglife.Impassible;
import huglife.Occupant;
import org.junit.Test;

import java.awt.Color;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.move();
        assertEquals(1.94, c.energy(), 0.01);
        c.stay();
        assertEquals(1.93, c.energy(), 0.01);
        c.stay();
        assertEquals(1.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus child = c.replicate();
        assertNotSame(c, child);
        assertEquals(1.0, c.energy(), 0.0001);
        assertEquals(1.0, child.energy(), 0.0001);
    }


    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.8);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        surrounded.put(Direction.RIGHT, new Empty());
        actual = c.chooseAction(surrounded);
        assertEquals(Action.ActionType.REPLICATE, actual.type);
        assertEquals(Direction.RIGHT, actual.dir);

        surrounded.put(Direction.LEFT, new Plip(1));
        actual = c.chooseAction(surrounded);
        assertEquals(Action.ActionType.ATTACK, actual.type);
        assertEquals(Direction.LEFT, actual.dir);

        surrounded.put(Direction.LEFT, new Impassible());
        actual = c.chooseAction(surrounded);
        assertEquals(Action.ActionType.REPLICATE, actual.type);
        assertEquals(Direction.RIGHT, actual.dir);
    }
}
