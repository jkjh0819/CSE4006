package faceduck.actors;

import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public abstract class Action implements Animal {
    public Action() {

    }

    public void move(World world, Direction dir) {
        if (world == null)
            throw new NullPointerException("World cannot be null.");
        if (dir == null)
            throw new NullPointerException("Dir cannot be null.");

        Location oldLoc = world.getLocation(this);
        Location newLoc = new Location(oldLoc, dir);
        if((world.isValidLocation(newLoc)) && (world.getThing(newLoc) == null) )
        {
            world.remove(this);
            world.add(this, newLoc);
        }
    }
}
