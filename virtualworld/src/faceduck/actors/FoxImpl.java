package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.interfaces.Rabbit;

public class FoxImpl extends Action implements Fox {
    private static final int FOX_MAX_ENERGY = 160;
    private static final int FOX_VIEW_RANGE = 5;
    private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
    private static final int FOX_COOL_DOWN = 2;
    private static final int FOX_INITAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;

    private int foxEnergy = FOX_INITAL_ENERGY;
    private FoxAI foxAI = new FoxAI();

    @Override
    public void act(World world) {
        Command cmd = this.foxAI.act(world, this);
        foxEnergy -= 1;
        cmd.execute(world, this);
        if (foxEnergy <= 0)
            world.remove(this);
    }

    @Override
    public int getViewRange() {
        return FOX_VIEW_RANGE;
    }

    @Override
    public int getCoolDown() {
        return FOX_COOL_DOWN;
    }

    @Override
    public int getEnergy() {
        return foxEnergy;
    }

    @Override
    public int getMaxEnergy() {
        return FOX_MAX_ENERGY;
    }

    @Override
    public int getBreedLimit() {
        return FOX_BREED_LIMIT;
    }


    @Override
    public void eat(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        Rabbit food = (Rabbit) world.getThing(newLoc);
        foxEnergy += food.getEnergyValue();
        world.remove(food);
    }

    @Override
    public void breed(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        foxEnergy = foxEnergy / 2;

        FoxImpl child = new FoxImpl(foxEnergy);
        world.add(child, newLoc);
    }

    public FoxImpl(){

    }

    protected FoxImpl(int energy) {
        foxEnergy = energy;
    }
}
