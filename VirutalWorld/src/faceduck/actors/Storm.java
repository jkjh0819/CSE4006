package faceduck.actors;

import faceduck.ai.StormAI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Location;

import java.util.Random;

/**
 * Storm is an {@link Actor}. Storm moves randomly.
 * When it moves, it remains eaten object to previous location and eats an object in new location.
 */
public class Storm implements Actor {

    private static final int VIEW_RANGE = 40;
    private static final int COOL_DOWN = 20;

    //variable for executing command
    private StormAI ai;
    private Object insideObject;

    /**
     * constructor for Storm, it initialize ai and object.
     */
    public Storm() {
        ai = new StormAI();
        insideObject = null;
    }

    /**
     * Get a command to execute from ai and execute it.
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

        Command cmd = this.ai.act(world, this);
        cmd.execute(world, this);
    }

    /**
     * Returns view range value of storm.
     *
     * @return an Integer representing view range.
     */
    @Override
    public int getViewRange() {
        return VIEW_RANGE;
    }

    /**
     * Returns cool down value of storm.
     *
     * @return an Integer representing cool down.
     */
    @Override
    public int getCoolDown() {
        return COOL_DOWN;
    }

    /**
     * Returns a location to move regardless of existence of actor.
     *
     * @param world
     *          The world to inspect and move.
     *
     * @return a location to move.
     */
    private Location randomLoc(World world) {
        Random rand = new Random();
        int x = rand.nextInt(world.getWidth());
        int y = rand.nextInt(world.getHeight());

        return new Location(x, y);
    }

    /**
     * Storm moves to random location.
     * When it moves, it remains eaten object to previous location and eats an object in new location.
     *
     * @param world
     *          The world to inspect and move.
     */
    public void summon(World world) {
        Location oldLoc = world.getLocation(this);
        world.remove(this);

        //if eaten object exist, remain it in previous location.
        if (insideObject != null)
            world.add(insideObject, oldLoc);

        //move to random location
        Location newLoc = randomLoc(world);

        //if there is an object, storm eats it.
        insideObject = world.getThing(newLoc);
        if (insideObject != null) {
            world.remove(insideObject);
        }
        world.add(this, newLoc);
    }

}
