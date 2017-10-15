package faceduck.actors;

import faceduck.ai.StormAI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Location;
import java.util.Random;

public class Storm implements Actor {

  private static final int VIEW_RANGE = 40;
  private static final int COOL_DOWN = 3;

  private StormAI ai;

  public Storm() {
    ai = new StormAI();
  }

  @Override
  public void act(World world) {
    if (world == null) {
      throw new NullPointerException("World must not be null.");
    }

    Command cmd = this.ai.act(world, this);
    cmd.execute(world, this);
  }

  @Override
  public int getViewRange() {
    return VIEW_RANGE;
  }

  @Override
  public int getCoolDown() {
    return COOL_DOWN;
  }

  private Location randomLoc(World world) {
    Random rand = new Random();
    int x = rand.nextInt(world.getWidth());
    int y = rand.nextInt(world.getHeight());

    return new Location(x, y);
  }

  public void summon(World world) {
    Location oldLoc = world.getLocation(this);
    world.remove(this);

    Location newLoc = randomLoc(world);
    Object insideActor = world.getThing(newLoc);
    if (insideActor != null) {
      world.remove(insideActor);
    }
    world.add(this, newLoc);
  }

}
