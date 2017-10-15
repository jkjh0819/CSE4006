package faceduck.ai;

import faceduck.actors.FoxImpl;
import faceduck.actors.RabbitImpl;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

public class FoxAI extends AbstractAI implements AI {

    private static final double RABBIT_WEIGHT = 20;

    /**
     * constructor for FoxAI
     */
    public FoxAI() {
    }

    @Override
    public Command act(World world, Actor actor) {
        if (actor == null)
            throw new NullPointerException("Actor cannot be null.");
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (!(actor instanceof FoxImpl))
            throw new IllegalArgumentException("Actor should be Fox.");

        FoxImpl fox = (FoxImpl) actor;

        int energy = fox.getEnergy();
        Location oldLoc = world.getLocation(actor);

        if (energy > fox.getBreedLimit()) {
            Direction dir = emptyAdjacentDir(world, oldLoc);
            if(dir != null)
                return new BreedCommand(dir);
        } else {
            Location newLoc = findAdjacentObject(world, oldLoc, ActorType.RABBIT);
            if(newLoc != null){
                return new EatCommand(oldLoc.dirTo(newLoc));
            }
        }

        double maxWeight = 0;
        Location preferLoc = oldLoc;

        for (int x = -fox.getViewRange(); x < fox.getViewRange(); x++) {
            for (int y = -fox.getViewRange(); y < fox.getViewRange(); y++) {
                Location newLoc = new Location(oldLoc.getX() + x, oldLoc.getY() + y);
                double weight = 0;
                if (world.isValidLocation(newLoc)) {
                    if(typeActor(world.getThing(newLoc)) == ActorType.RABBIT)
                            weight += RABBIT_WEIGHT * ( 1.0 / oldLoc.distanceTo(newLoc));

                    if(maxWeight < weight) {
                        maxWeight = weight;
                        preferLoc = newLoc;
                    }
                }
            }
        }

        if(preferLoc != oldLoc) {
            if(world.getThing(new Location(oldLoc, oldLoc.dirTo(preferLoc))) == null)
                return new MoveCommand(oldLoc.dirTo(preferLoc));
        }

        return new MoveCommand(Util.randomDir());
    }
}
