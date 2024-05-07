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
 * An implementation of a fierce BLUE-colored predator that enjoys nothing
 * more than snacking on hapless Plips.
 */
public class Clorus extends Creature {
    private static final int RED = 34;
    private static final int GREEN = 0;
    private static final int BLUE = 231;
    private static final double MOVE_ENERGY_COST = 0.03;
    private static final double STAY_ENERGY_COST = 0.01;
    private static final double REPLICATE_ENERGY = 1.0;
    private static final double REP_ENERGY_RETAINED = 0.5;
    private static final double REP_ENERGY_GIVEN = 0.5;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    /**
     * creates clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    /**
     * Always return a color with RED = 34; GREEN = 0; BLUE = 231;
     */
    public Color color() {
        return color(RED, GREEN, BLUE);
    }

    /**
     * Eat C and gain it's energy.
     */
    public void attack(Creature c) {
        energy += c.energy();
    }


    /**
     * clorus should lose 0.03 units of energy when moving.
     */
    public void move() {
        energy -= MOVE_ENERGY_COST;
    }

    /**
     * clorus lose 0.01 energy when staying due to photosynthesis.
     */
    public void stay() {
        energy -= STAY_ENERGY_COST;
    }

    /**
     * Clorus and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Clorus.
     */
    public Clorus replicate() {
        Clorus child = new Clorus(energy * REP_ENERGY_GIVEN);
        energy = energy * REP_ENERGY_RETAINED;
        return child;
    }

    /**
     * Clorus take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if any Plips, attack one of them randomly.
     * 3. Otherwise, if energy >= 1, REPLICATE.
     * 4. Otherwise, move to a random empty square.
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

        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (plips.size() > 0) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }

        if (energy >= REPLICATE_ENERGY) {
            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empties));
        }

        return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(empties));
    }
}
