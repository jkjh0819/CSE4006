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
import faceduck.skeleton.util.Util;

import java.util.Random;

public class RabbitAI extends AbstractAI implements AI {

    private static final double GRASS_WEIGHT = 10;
    private static final double GNAT_WEIGHT = -2;
    private static final double FOX_WEIGHT = -20;
    private static final double RABBIT_WEIGHT = -1;
    private static final int BRRED_PROB_MAX = 10;
    private static final int BREED_PROB = 5;

    /**
     * constructor for RabbitAI
     */
    public RabbitAI() {
    }

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

        Random rand = new Random();

        RabbitImpl rabbit = (RabbitImpl) actor;

        int energy = rabbit.getEnergy();
        Location oldLoc = world.getLocation(actor);

        if (energy > rabbit.getBreedLimit()) {
            Direction dir = emptyAdjacentDir(world, oldLoc);
            if (dir != null && rand.nextInt(BRRED_PROB_MAX) < BREED_PROB) {
                return new BreedCommand(dir);
            }
        } else {
            Location newLoc = findAdjacentObject(world, oldLoc, ActorType.GRASS);
            if (newLoc != null) {
                return new EatCommand(oldLoc.dirTo(newLoc));
            }
        }

        double maxWeight = Integer.MIN_VALUE;
        Location preferLoc = oldLoc;

        for (int x = -rabbit.getViewRange(); x < rabbit.getViewRange(); x++) {
            for (int y = -rabbit.getViewRange(); y < rabbit.getViewRange(); y++) {
                Location newLoc = new Location(oldLoc.getX() + x, oldLoc.getY() + y);
                double weight;
                if (world.isValidLocation(newLoc)) {
                    weight = calWeight(world, newLoc);

                    if (maxWeight < weight) {
                        maxWeight = weight;
                        preferLoc = newLoc;
                    }
                }
            }
        }

        if (preferLoc != oldLoc) {
            Location newLoc = new Location(oldLoc, oldLoc.dirTo(preferLoc));
            if (world.isValidLocation(newLoc)) {
                if (world.getThing(new Location(oldLoc, oldLoc.dirTo(preferLoc))) == null) {
                    return new MoveCommand(oldLoc.dirTo(preferLoc));
                }
            } else {
                if (emptyAdjacentDir(world, oldLoc) != null)
                    return new MoveCommand(emptyAdjacentDir(world, oldLoc));
            }

        }

        return new MoveCommand(Util.randomDir());
    }

    private double calWeight(World world, Location loc) {
        double weight = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Location tmpLoc = new Location(loc.getX() + x, loc.getY() + y);
                if (world.isValidLocation(tmpLoc)) {
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
