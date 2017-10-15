package faceduck.ai;

import faceduck.commands.SummonCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;

public class StormAI extends AbstractAI implements AI {

    /**
     * constructor for StormAI
     */
    public StormAI() {

    }

    /**
     * Returns a command to execute
     *
     * @param world
     *          The world to inspect.
     * @param actor
     *          The actor to consider.
     * @return SummonCommand, it place outObject to old location and get inObject from new location.
     */
    @Override
    public Command act(World world, Actor actor) {
        return new SummonCommand();
    }

}
