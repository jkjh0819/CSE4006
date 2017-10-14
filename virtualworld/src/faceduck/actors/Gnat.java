package faceduck.actors;

import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat extends AbstractAnimal implements Animal {
    private static final int MAX_ENERGY = 10;
    private static final int VIEW_RANGE = 1;
    private static final int BREED_LIMIT = 0;
    private static final int COOL_DOWN = 0;


    public Gnat(int n) {
        ai = new GnatAI();
    }

    @Override
    public int getViewRange() {
        return VIEW_RANGE;
    }

    @Override
    public int getCoolDown() {
        return COOL_DOWN;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    @Override
    public int getBreedLimit() {
        return BREED_LIMIT;
    }

    @Override
    public Animal makeChild(int initEnergy) {
        return null;
    }
}
