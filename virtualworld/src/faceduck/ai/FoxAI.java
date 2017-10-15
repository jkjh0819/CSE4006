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

public class FoxAI extends AbstractAI implements AI {

    private static final double RABBIT_WEIGHT = 20;
    private static final int HUNGRY_MAX = 20;

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

        Random rand = new Random();
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
                double hungry = (double)fox.getMaxEnergy() / fox.getEnergy();
                if(hungry > rand.nextInt(HUNGRY_MAX))
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

        return moveTo(world, oldLoc, preferLoc);
    }


}
