package faceduck.actors;

import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat extends AbstractAnimal implements Animal {
    private static final int MAX_ENERGY = 10;
    private static final int VIEW_RANGE = 1;
    private static final int BREED_LIMIT = 0;
    private static final int COOL_DOWN = 0;
    private static final int MAX_AGE = 0;

    /**
     * constructor for Gnat. it initialize ai, energy, age as initial value.
     */

    public Gnat(){
        ai = new GnatAI();
    }

    public Gnat(int n) {
        ai = new GnatAI();
    }

    /**
     * Get a command to execute from ai and execute it.
     * Gnat does not lose energy and age
     *
     * @param world
     *            The world that the actor is currently in.
     */
    @Override
    public void act(World world) {
        if (world == null) {
            throw new NullPointerException("World must not be null.");
        }

        Command cmd = this.ai.act(world, this);
        cmd.execute(world, this);
    }

    /**
     * Return gnat's view range.
     *
     * @return an Integer representing gnat view range.
     */
    @Override
    public int getViewRange() {
        return VIEW_RANGE;
    }

    /**
     * Return gnat's cool down.
     *
     * @return an Integer representing gnat cool down.
     */
    @Override
    public int getCoolDown() {
        return COOL_DOWN;
    }

    /**
     * Return gnat's max energy.
     *
     * @return an Integer representing gnat max energy.
     */
    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    /**
     * Return gnat's breed limit.
     *
     * @return an Integer representing gnat breed limit.
     */
    @Override
    public int getBreedLimit() {
        return BREED_LIMIT;
    }


    /**
     * Gnat cannot breed, so it does not make child.
     *
     * @param initEnergy
     *          initial energy value of child
     * @return null, Gnat cannot breed.
     */
    @Override
    public Animal makeChild(int initEnergy) {
        return null;
    }

    /**
     * Gnat is not aged.
     */
    @Override
    protected void aging(){
        return;
    }

    /**
     * Return gnat's max age.
     *
     * @return an Integer representing gnat max age.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }
}
