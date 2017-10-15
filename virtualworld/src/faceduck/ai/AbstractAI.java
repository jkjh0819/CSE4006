package faceduck.ai;

import faceduck.actors.*;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

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
        } else if (object instanceof Storm){
            return ActorType.STORM;
        } else if (object == null) {
            return null;
        } else
            throw new IllegalArgumentException("Actor is not defined.");

    }

    protected Direction emptyAdjacentDir(World world, Location loc) {
        Random rand = new Random();
        int index = rand.nextInt(Direction.values().length);

        for (int i = 0; i < Direction.values().length; ++i) {
            index = ((index + 1) % Direction.values().length);
            Location newLoc = new Location(loc, Direction.values()[index]);
            if (world.isValidLocation(newLoc) && world.getThing(newLoc) == null)
                return Direction.values()[index];
        }
        return null;
    }

    protected Location findAdjacentObject(World world, Location loc, ActorType actorType) {
        Random rand = new Random();
        int index = rand.nextInt(Direction.values().length);

        for (int i = 0; i < Direction.values().length; ++i) {
            index = ((index + 1) % Direction.values().length);
            Location newLoc = new Location(loc, Direction.values()[i]);
            if (world.isValidLocation(newLoc) && typeActor(world.getThing(newLoc)) == actorType)
                return newLoc;
        }
        return null;
    }

}
