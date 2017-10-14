package faceduck.actors;

import faceduck.ai.AbstractAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Edible;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public abstract class AbstractAnimal implements Animal {

    private static final int ACTION_POINT = 1;
    private static final int DYING_LEVEL = 0;

    protected int energy;
    protected AbstractAI ai;

    public AbstractAnimal() {

    }

    @Override
    public void act(World world) {
        if (world == null) {
            throw new NullPointerException("World must not be null.");
        }

        Command cmd = this.ai.act(world, this);
        if (cmd != null) {
            if (!(this instanceof Gnat)) {
                energy -= ACTION_POINT;
                cmd.execute(world, this);
                if (energy <= DYING_LEVEL)
                    world.remove(this);
            } else {
                cmd.execute(world, this);
            }
        }
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void eat(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        Edible food = (Edible) world.getThing(newLoc);
        earnEnergy(food.getEnergyValue());
        world.remove(food);
    }

    @Override
    public void move(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);
        if ((world.isValidLocation(newLoc)) && (world.getThing(newLoc) == null)) {
            world.remove(this);
            world.add(this, newLoc);
        }
    }

    @Override
    public void breed(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        energy = energy / 2;

        world.add(makeChild(energy), newLoc);
    }

    protected abstract Animal makeChild(int initEnergy);

    private void earnEnergy(int newEnergy) {
        energy += newEnergy;
        energy = newEnergy < getMaxEnergy() ? newEnergy : getMaxEnergy();
    }
}
