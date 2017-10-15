package faceduck.actors;

import static java.lang.Math.min;

import faceduck.ai.AbstractAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Edible;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * The Animal for abstracting other Animals. It includes some implementations of animals
 * It includes abstract method for return sub classes variables.
 */
public abstract class AbstractAnimal implements Animal {

    //variable for calculating energy and deciding death.
    private static final int ACTION_POINT = 1;
    private static final int DYING_LEVEL = -1;

    protected int energy;
    protected int age;
    protected AbstractAI ai;

    /**
     * zero constructor for AbstractAnimal.
     */
    public AbstractAnimal() {

    }

    /**
     * Get a command to execute from ai and execute it.
     * When the command is executed, lose one unit of energy.
     * If energy reaches to dying level(0), it dies.
     * If age is over max age, it dies.
     * When it dies, it removes itself from world.
     *
     * @param world
     *            The world that the actor is currently in.
     *
     */
    @Override
    public void act(World world) {
        if (world == null) {
            throw new NullPointerException("World must not be null.");
        }

        //if act called, lose one unit of energy and get one age.
        energy -= ACTION_POINT;
        aging();

        Command cmd = this.ai.act(world, this);
        if (cmd != null) {
            cmd.execute(world, this);
            //Gnat energy starts at -1, so it never dies.
            //if age is over max, it dies.
            if (energy == DYING_LEVEL || age > this.getMaxAge())
                world.remove(this);
        }
    }

    /**
     * Return subclass's view range.
     *
     * @return an Integer representing view range.
     */
    public abstract int getViewRange();

    /**
     * Return subclass's cool down.
     *
     * @return an Integer representing cool down.
     */
    public abstract int getCoolDown();

    /**
     * Return energy value.
     *
     * @return an Integer representing current energy.
     */
    @Override
    public int getEnergy() {
        return energy;
    }

    /**
     * Return subclass's max energy value.
     *
     * @return an Integer representing max energy value.
     */
    public abstract int getMaxEnergy();

    /**
     * Return subclass's breed limit.
     *
     * @return an Integer representing breed limit.
     */
    public abstract int getBreedLimit();

    /**
     * Increase energy by eating edible object at the direction,
     *
     * @param world
     *            The world containing this actor.
     * @param dir
     *            The direction of the Actor to eat.
     *
     */
    @Override
    public void eat(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        //food existence is guaranteed at AI level.
        Edible food = (Edible) world.getThing(newLoc);

        //After earn energy, it is not over max.
        earnEnergy(food.getEnergyValue());
        world.remove(food);
    }

    /**
     * Move to the direction.
     *
     * @param world
     *            The world containing this actor.
     * @param dir
     *            The direction to move in.
     *
     */
    @Override
    public void move(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        //if new location is not valid or not empty, it does not move.
        if ((world.isValidLocation(newLoc)) && (world.getThing(newLoc) == null)) {
            world.remove(this);
            world.add(this, newLoc);
        }
    }

    /**
     * Breed child and energy value will be 1/2.(round down)
     *
     * @param world
     *            The world containing this actor.
     * @param dir
     *            The direction in which the new Animal will spawn.
     *
     */
    @Override
    public void breed(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);

        energy = energy / 2;

        //Whether new location is empty is guaranteed at AI level.
        //add child to world with init energy(1/2 of parent).
        world.add(makeChild(energy), newLoc);
    }

    /**
     * Make child object which is same to parent.
     *
     * @param initEnergy
     *          initial energy value of child
     * @return an Animal object with same type and energy of parent object
     */
    protected abstract Animal makeChild(int initEnergy);

    /**
     * Add energy to current energy. It is not over max energy.
     *
     * @param newEnergy
     *          energy value to earn
     */
    private void earnEnergy(int newEnergy) {
        energy += newEnergy;
        energy = min(energy, getMaxEnergy());
    }

    /**
     * Get age when object act.
     */
    protected void aging(){
        age++;
    }

    /**
     * Return subclass's max age.
     *
     * @return an Integer representing max age.
     */
    protected abstract int getMaxAge();
}
