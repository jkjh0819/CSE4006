package faceduck.actors;

import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Animal;
/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat extends AbstractAnimal implements Animal {
    private static final int MAX_ENERGY = -1;
    private static final int VIEW_RANGE = 1;
    private static final int BREED_LIMIT = 0;
    private static final int COOL_DOWN = 0;
    private static final int MAX_AGE = 0;

    public Gnat(int n) {
        ai = new GnatAI();
        energy = MAX_ENERGY;
        age = -1;
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

    @Override
    protected void aging(){
        return;
    }

    protected int getMaxAge(){
        return MAX_AGE;
    }
}
