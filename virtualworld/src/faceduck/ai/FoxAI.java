package faceduck.ai;

import faceduck.actors.FoxImpl;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

import java.util.Random;

/**
 * The AI for a Fox. This AI will pick a direction for following rabbit or decide to breed or to eat rabbit in adjacent location.
 * and then return a command which executes the decision.
 */
public class FoxAI extends AbstractAI implements AI {

    private static final double RABBIT_WEIGHT = 20;
    private static final int HUNGRY_MAX = 13;

    /**
     * constructor for FoxAI
     */
    public FoxAI() {
    }

    /**
     * Returns a command to execute. It calculates weight of a location and prefer to move to highest weight.
     *
     * @param world
     *          The world to inspect.
     * @param actor
     *          The actor to consider.
     * @return a Command among Breed, Move, Eat Command.
     */
    @Override
    public Command act(World world, Actor actor) {
        if (actor == null)
            throw new NullPointerException("Actor cannot be null.");
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (!(actor instanceof FoxImpl))
            throw new IllegalArgumentException("Actor should be Fox.");

        //get basic value for calculate weight
        Random rand = new Random();
        FoxImpl fox = (FoxImpl) actor;
        int energy = fox.getEnergy();
        Location oldLoc = world.getLocation(actor);

        //if energy satisfied breed limit,
        if (energy > fox.getBreedLimit()) {
            Direction dir = emptyAdjacentDir(world, oldLoc);
            //if there is a empty adjacent location, it should breed to the adjacent location.
            if (dir != null)
                return new BreedCommand(dir);
        } else {
            //if rabbit is in adjacent location and fox is hungry, eat
            Location newLoc = findAdjacentObject(world, oldLoc, ActorType.RABBIT);
            if (newLoc != null) {
                //this part decide whether fox is hungry or not.
                double hungry = (double) fox.getMaxEnergy() / fox.getEnergy();

                //rand.nextInt(HUNGRY_MAX) decide whether fox eats rabbit or not.
                if (hungry > rand.nextInt(HUNGRY_MAX))
                    return new EatCommand(oldLoc.dirTo(newLoc));
            }
        }

        //if there is no rabbit in adjacent location or decide not to eat rabbit or not to breed
        //find a location close to rabbit.
        double maxWeight = 0;
        Location preferLoc = oldLoc;

        for (int x = -fox.getViewRange(); x < fox.getViewRange(); x++) {
            for (int y = -fox.getViewRange(); y < fox.getViewRange(); y++) {
                Location newLoc = new Location(oldLoc.getX() + x, oldLoc.getY() + y);
                double weight = 0;
                //if location seen is valid
                if (world.isValidLocation(newLoc)) {
                    //calc weight
                    if (typeActor(world.getThing(newLoc)) == ActorType.RABBIT)
                        weight += RABBIT_WEIGHT * (1.0 / oldLoc.distanceTo(newLoc));

                    if (maxWeight < weight) {
                        maxWeight = weight;
                        preferLoc = newLoc;
                    }
                }
            }
        }

        //move to preferLocation. If there is no location to move, return Random move command.
        return moveTo(world, oldLoc, preferLoc);
    }


}
