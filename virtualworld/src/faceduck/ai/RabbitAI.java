package faceduck.ai;

import faceduck.actors.Grass;
import faceduck.actors.RabbitImpl;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

public class RabbitAI extends AbstractAI implements AI {

	private static final double GRASS_WEIGHT = 5;

	/**
	 * constructor for RabbitAI
	 */
	public RabbitAI() {
	}

	@Override
	public Command act(World world, Actor actor) {
		if (actor == null)
			throw new NullPointerException("Actor cannot be null.");
		if (world == null)
			throw new NullPointerException("World cannot be null.");
		if (!(actor instanceof RabbitImpl))
			throw new ClassCastException("Actor should be Rabbit.");

		RabbitImpl rabbit = (RabbitImpl) actor;

		int energy = rabbit.getEnergy();
		Location oldLoc = world.getLocation(actor);

		if (energy > rabbit.getBreedLimit()) {
			Location newLoc = emptyAdjacentLoc(world, oldLoc);
			if(newLoc != null)
				return new BreedCommand(oldLoc.dirTo(newLoc));
		} else {
			//바로 다 먹어버리는 부분 변경
			Location newLoc = findAdjacentObject(world, oldLoc, ActorType.GRASS);
			if(newLoc != null){
				return new EatCommand(oldLoc.dirTo(newLoc));
			}
		}

		double maxWeight = 0;
		Location preferLoc = oldLoc;

		for (int x = -rabbit.getViewRange(); x < rabbit.getViewRange(); x++) {
			for (int y = -rabbit.getViewRange(); y < rabbit.getViewRange(); y++) {
				Location newLoc = new Location(oldLoc.getX() + x, oldLoc.getY() + y);
				double weight = 0;
				if (world.isValidLocation(newLoc)) {
					if(typeActor(world.getThing(newLoc)) == ActorType.GRASS)
						weight += GRASS_WEIGHT * ( 1.0 / oldLoc.distanceTo(newLoc));

					if(maxWeight < weight) {
						maxWeight = weight;
						preferLoc = newLoc;
					}
				}
			}
		}

		if(preferLoc != oldLoc)
			return new MoveCommand(oldLoc.dirTo(preferLoc));

		return null;
	}

}
