package faceduck.ai;

import faceduck.actors.*;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

import java.util.Random;

public abstract class AbstractAI implements AI {

    /**
     * constructor for AbstractAI
     */
    public AbstractAI() {

    }

    /**
     * abstract act command
     */
    public abstract Command act(World world, Actor actor);

    /**
     * Returns a type of Actor object
     *
     * @param object
     *          object for deciding what type is.
     * @return a enum value that means what type is.
     */
    protected ActorType typeActor(Object object) {
        if (object instanceof FoxImpl) {
            return ActorType.FOX;
        } else if (object instanceof RabbitImpl) {
            return ActorType.RABBIT;
        } else if (object instanceof Gnat) {
            return ActorType.GNAT;
        } else if (object instanceof Grass) {
            return ActorType.GRASS;
        } else if (object instanceof Gardener) {
            return ActorType.GARDENER;
        } else if (object instanceof Storm) {
            return ActorType.STORM;
        } else if (object == null) {
            return null;
        } else
            throw new IllegalArgumentException("Actor is not defined.");

    }

    /**
     * Returns a direction to point empty adjacent location.
     *
     * @param world
     *          The world to inspect.
     * @param loc
     *          The location to be center.
     *
     * @return a direction pointing to empty adjacent location. if there isn't return null.
     */
    protected Direction emptyAdjacentDir(World world, Location loc) {
        Random rand = new Random();
        int index = rand.nextInt(Direction.values().length);

        for (int i = 0; i < Direction.values().length; ++i) {
            index = ((index + 1) % Direction.values().length);
            Location newLoc = new Location(loc, Direction.values()[index]);
            //if new location is in world and empty,
            if (world.isValidLocation(newLoc) && world.getThing(newLoc) == null)
                return Direction.values()[index];
        }
        return null;
    }

    /**
     * Returns a location that an actor to find is located.
     *
     * @param world
     *          The world to inspect.
     * @param loc
     *          The location to be center.
     * @param actorType
     *          The type of actor to find.
     *
     * @return a location that an actor to find is located. If there isn't return null.
     */
    protected Location findAdjacentObject(World world, Location loc, ActorType actorType) {
        Random rand = new Random();
        int index = rand.nextInt(Direction.values().length);

        for (int i = 0; i < Direction.values().length; ++i) {
            index = ((index + 1) % Direction.values().length);
            Location newLoc = new Location(loc, Direction.values()[i]);
            //if new location is in world and there is an actor to find,
            if (world.isValidLocation(newLoc) && typeActor(world.getThing(newLoc)) == actorType)
                return newLoc;
        }
        return null;
    }

    /**
     * Returns a move command for moving to preferred location.
     *
     * @param world
     *          The world to inspect.
     * @param oldLoc
     *          The location that locates currently.
     * @param preferLoc
     *          The location that prefer to move.
     *
     * @return a move command that is able to move for going to preferred location.
     *          If there isn't, return a random move command.
     */
    protected Command moveTo(World world, Location oldLoc, Location preferLoc) {
        if (preferLoc != oldLoc) {
            Location newLoc = new Location(oldLoc, oldLoc.dirTo(preferLoc));
            //if preferred location is valid
            if (world.isValidLocation(newLoc)) {
                //and empty, move to the location
                if (world.getThing(new Location(oldLoc, oldLoc.dirTo(preferLoc))) == null) {
                    return new MoveCommand(oldLoc.dirTo(preferLoc));
                }
            } else {
                //if preferred location is not valid, move to empty adjacent location.
                if (emptyAdjacentDir(world, oldLoc) != null)
                    return new MoveCommand(emptyAdjacentDir(world, oldLoc));
            }
        }

        return new MoveCommand(Util.randomDir());
    }

}
