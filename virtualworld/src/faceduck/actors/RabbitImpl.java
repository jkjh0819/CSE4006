package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.interfaces.Edible;

public class RabbitImpl extends Action implements Rabbit {
    private static final int RABBIT_MAX_ENERGY = 20;
    private static final int RABBIT_VIEW_RANGE = 3;
    private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 2 / 4;
    private static final int RABBIT_ENERGY_VALUE = 20;
    private static final int RABBIT_COOL_DOWN = 4;
    private static final int RABBIT_INITAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;

    private int rabbitEnergy = RABBIT_INITAL_ENERGY;
    private RabbitAI rabbitAI = new RabbitAI();

    @Override
    public void act(World world) {
        Command cmd = this.rabbitAI.act(world, this);
        rabbitEnergy -= 1;
        cmd.execute(world, this);
        //die check requested
    }

    @Override
    public int getViewRange() {
        return RABBIT_VIEW_RANGE;
    }

    @Override
    public int getCoolDown() {
        return RABBIT_COOL_DOWN;
    }

    @Override
    public int getEnergy() {
        return rabbitEnergy;
    }

    @Override
    public int getEnergyValue() {
        return RABBIT_ENERGY_VALUE;
    }

    @Override
    public int getMaxEnergy() {
        return RABBIT_MAX_ENERGY;
    }

    @Override
    public int getBreedLimit() {
        return RABBIT_BREED_LIMIT;
    }

    @Override
    public void eat(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        Grass food = (Grass)world.getThing(newLoc);
        rabbitEnergy += food.getEnergyValue();
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

        rabbitEnergy  = rabbitEnergy / 2;

        RabbitImpl child = new RabbitImpl(rabbitEnergy);
        world.add(child, newLoc);
    }

    public RabbitImpl(){

    }

    protected RabbitImpl(int energy){
        rabbitEnergy = energy;
    }
}
