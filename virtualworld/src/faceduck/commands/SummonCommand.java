package faceduck.commands;

import faceduck.actors.Storm;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;

/**
 * This command calls an {@link Storm} to breed.
 */
public class SummonCommand implements Command {

  /**
   * Instantiates a summon command to be executed.
   */
  public SummonCommand(){
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException
   *             If actor is not an instance of Storm.
   */
  @Override
  public void execute(World world, Actor actor) {
    if (actor == null) {
      throw new NullPointerException("Actor cannot be null");
    } else if (world == null) {
      throw new NullPointerException("World cannot be null");
    } else if (!(actor instanceof Storm)) {
      throw new IllegalArgumentException(
          "actor must be an instance of Storm.");
    }
    ((Storm) actor).summon(world);
  }
}
