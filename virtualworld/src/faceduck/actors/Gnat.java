package faceduck.actors;

import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * This is a simple implementation of a Gnat. It never loses energy and moves in
 * random directions.
 */
public class Gnat extends Action implements Animal {
    private static final int MAX_ENERGY = 10;
    private static final int VIEW_RANGE = 1;
    private static final int BREED_LIMIT = 0;
    private static final int COOL_DOWN = 0;

    private GnatAI gnatAI = new GnatAI();

    public Gnat(int n) {
    }

    @Override
    public void act(World world) {
        Command cmd = this.gnatAI.act(world, this);
        cmd.execute(world, this);
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
    public void eat(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");
    }

    @Override
    public void breed(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

    }


}
