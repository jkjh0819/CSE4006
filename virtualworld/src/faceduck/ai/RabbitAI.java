package faceduck.ai;

import faceduck.actors.RabbitImpl;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

import java.util.Random;

public class RabbitAI extends AbstractAI implements AI {

    //weight for calculate Location preference
    private static final double GRASS_WEIGHT = 10;
    private static final double GNAT_WEIGHT = -2;
    private static final double FOX_WEIGHT = -20;
    private static final double RABBIT_WEIGHT = -1;

    //constant to suppress breed
    private static final int BREED_PROB_MAX = 10;
    private static final int BREED_PROB = 5;

    /**
     * constructor for RabbitAI
     */
    public RabbitAI() {
    }

    /**
     *  Returns a command to execute. It calculates weight of a location and prefer to move to highest weight.
     *
     * @param world
     *            The world to inspect.
     * @param actor
     *            The actor to consider.
     *
     * @return a Command among Breed, Move, Eat Command.
     */
    @Override
    public Command act(World world, Actor actor) {
        if (actor == null) {
            throw new NullPointerException("Actor cannot be null.");
        }
        if (world == null) {
            throw new NullPointerException("World cannot be null.");
        }
        if (!(actor instanceof RabbitImpl)) {
            throw new IllegalArgumentException("Actor should be Rabbit.");
        }

        //get basic value for calculate weight
        Random rand = new Random();
        RabbitImpl rabbit = (RabbitImpl) actor;
        int energy = rabbit.getEnergy();
        Location oldLoc = world.getLocation(actor);

        //if energy satisfied breed limit,
        if (energy > rabbit.getBreedLimit()) {
            //breed to adjacent location by probability BREED_PROB / BREED_PROB_MAX
            Direction dir = emptyAdjacentDir(world, oldLoc);
            if (dir != null && rand.nextInt(BREED_PROB_MAX) < BREED_PROB) {
                return new BreedCommand(dir);
            }
        } else {
            //if Grass is in adjacent location, eat
            Location newLoc = findAdjacentObject(world, oldLoc, ActorType.GRASS);
            if (newLoc != null) {
                return new EatCommand(oldLoc.dirTo(newLoc));
            }
        }

        //there is no Grass in adjacent location. find highest weight location.
        double maxWeight = Integer.MIN_VALUE;
        Location preferLoc = oldLoc;

        for (int x = -rabbit.getViewRange(); x < rabbit.getViewRange(); x++) {
            for (int y = -rabbit.getViewRange(); y < rabbit.getViewRange(); y++) {
                Location newLoc = new Location(oldLoc.getX() + x, oldLoc.getY() + y);
                double weight;
                //if location seen is valid
                if (world.isValidLocation(newLoc)) {
                    //calc weight
                    weight = calWeight(world, newLoc);

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

    /**
     *  Calculate preference of a location by adjacent object.
     *
     * @param world
     *          The world to inspect.
     * @param loc
     *          The location to calculate weight
     *
     * @return a double value that means preference
     */
    private double calWeight(World world, Location loc) {
        double weight = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Location tmpLoc = new Location(loc.getX() + x, loc.getY() + y);
                //if location seen is valid
                if (world.isValidLocation(tmpLoc)) {
                    //check which type of actor is, then calculate weight.
                    ActorType actor = typeActor(world.getThing(tmpLoc));
                    if (actor != null) {
                        switch (actor) {
                            case GRASS:
                                weight += GRASS_WEIGHT;
                                break;
                            case GNAT:
                                weight += GNAT_WEIGHT;
                                break;
                            case RABBIT:
                                weight += RABBIT_WEIGHT;
                                break;
                            case FOX:
                                weight += FOX_WEIGHT;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return weight;
    }
}
