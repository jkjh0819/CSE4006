package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class RabbitImpl extends AbstractAnimal implements Rabbit {
    private static final int RABBIT_MAX_ENERGY = 20;
    private static final int RABBIT_VIEW_RANGE = 3;
    private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 2 / 4;
    private static final int RABBIT_ENERGY_VALUE = 20;
    private static final int RABBIT_COOL_DOWN = 4;
    private static final int RABBIT_INITAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;

    private RabbitAI rabbitAI = new RabbitAI();

    public RabbitImpl(){
        energy = RABBIT_INITAL_ENERGY;
        ai = new RabbitAI();
    }

    protected RabbitImpl(int initEnergy){
        energy = initEnergy;
        ai = new RabbitAI();
    }

    @Override
    public int getViewRange() {
        return RABBIT_VIEW_RANGE;
    }

    @Override
    public int getCoolDown() {
        return RABBIT_COOL_DOWN;
    }

    @Override
    public int getEnergyValue() {
        return RABBIT_ENERGY_VALUE;
    }

    @Override
    public int getMaxEnergy() {
        return RABBIT_MAX_ENERGY;
    }

    @Override
    public int getBreedLimit() {
        return RABBIT_BREED_LIMIT;
    }

    @Override
    public Animal makeChild(int initEnergy) {
        return new RabbitImpl(initEnergy);
    }
}
