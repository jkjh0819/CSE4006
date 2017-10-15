package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Rabbit;

public class RabbitImpl extends AbstractAnimal implements Rabbit {
    private static final int RABBIT_MAX_ENERGY = 20;
    private static final int RABBIT_VIEW_RANGE = 3;
    private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 2 / 4;
    private static final int RABBIT_ENERGY_VALUE = 20;
    private static final int RABBIT_COOL_DOWN = 4;
    private static final int RABBIT_INITIAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;
    private static final int RABBIT_MAX_AGE = 50;

    public RabbitImpl(){
        energy = RABBIT_INITIAL_ENERGY;
        ai = new RabbitAI();
        age = 0;
    }

    protected RabbitImpl(int initEnergy){
        energy = initEnergy;
        ai = new RabbitAI();
        age = 0;
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
    protected Animal makeChild(int initEnergy) {
        return new RabbitImpl(initEnergy);
    }

    @Override
    protected int getMaxAge(){
        return RABBIT_MAX_AGE;
    }
}
