package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

import java.awt.Color;
import java.util.List;
import java.util.Map;

/**
 * An implementation of a motile pacifist photosynthesizer.
 *
 * @author Josh Hug
 */
public class Plip extends Creature {

    private static final double MOVE_ENERGY_COST = 0.15;
    private static final double STAY_ENERGY_GAIN = 0.2;
    private static final double MOVE_PROBABILITY = 0.5;
    private static final double MAXIMUM_ENERGY = 2.0;
    private static final int MAXIMUM_GREEN = 255;
    private static final double MINIMUM_ENERGY = 0.0;
    private static final int MINIMUM_GREEN = 63;
    private static final double SLOPE_GREEN =
        (MAXIMUM_GREEN - MINIMUM_GREEN) / (MAXIMUM_ENERGY - MINIMUM_ENERGY);
    private static final double INTERCEPT_GREEN = MINIMUM_GREEN - SLOPE_GREEN * MINIMUM_ENERGY;
    private static final double REPLICATE_ENERGY = 1.0;
    private static final double REP_ENERGY_RETAINED = 0.5;
    private static final double REP_ENERGY_GIVEN = 0.5;


    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates plip with energy equal to E.
     */
    public Plip(double e) {
        super("plip");
        r = 0;
        g = 0;
        b = 0;
        if (e > MAXIMUM_ENERGY) {
            e = MAXIMUM_ENERGY;
        }
        energy = e;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Plip() {
        this(1);
    }

    /**
     * Should return a color with red = 99, blue = 76, and green that varies
     * linearly based on the energy of the Plip. If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255. The green value should vary with energy
     * linearly in between these two extremes. It's not absolutely vital
     * that you get this exactly correct.
     */
    public Color color() {
        r = 99;
        g = (int) (SLOPE_GREEN * energy + INTERCEPT_GREEN);
        b = 76;

        return color(r, g, b);
    }

    /**
     * Do nothing with C, Plips are pacifists.
     */
    public void attack(Creature c) {
    }

    /**
     * Plips should lose 0.15 units of energy when moving. If you want to
     * to avoid the magic number warning, you'll need to make a
     * private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= MOVE_ENERGY_COST;
    }


    /**
     * Plips gain 0.2 energy when staying due to photosynthesis.
     */
    public void stay() {
        energy += STAY_ENERGY_GAIN;
        if (energy > MAXIMUM_ENERGY) {
            energy = MAXIMUM_ENERGY;
        }
    }

    /**
     * Plips and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Plip.
     */
    public Plip replicate() {
        Plip child = new Plip(energy * REP_ENERGY_GIVEN);
        energy = energy * REP_ENERGY_RETAINED;
        return child;
    }

    /**
     * Plips take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        if (energy >= REPLICATE_ENERGY) {
            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empties));
        }

        List<Direction> cloruses = getNeighborsOfType(neighbors, "clorus");
        if (cloruses.size() > 0) {
            if (HugLifeUtils.random() < MOVE_PROBABILITY) {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
        return new Action(Action.ActionType.STAY);
    }
}
