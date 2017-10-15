package faceduck.ai;

import faceduck.commands.SummonCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;

public class StormAI extends AbstractAI implements AI {

  public StormAI(){

  }

  @Override
  public Command act(World world, Actor actor) {
    return new SummonCommand();
  }

}
