package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Rabbit;

/**
 * RabbitImpl is an implementation of {@link Rabbit}.
 * Eat, Breed, Move is implemented at {@link AbstractAnimal}
 */
public class RabbitImpl extends AbstractAnimal implements Rabbit {
    private static final int RABBIT_MAX_ENERGY = 20;
    private static final int RABBIT_VIEW_RANGE = 3;
    private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 2 / 4;
    private static final int RABBIT_ENERGY_VALUE = 20;
    private static final int RABBIT_COOL_DOWN = 4;
    private static final int RABBIT_INITIAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;
    private static final int RABBIT_MAX_AGE = 20;

    /**
     * zero constructor for RabbitImpl, it initialize energy, ai, age as initial value.
     */
    public RabbitImpl() {
        energy = RABBIT_INITIAL_ENERGY;
        ai = new RabbitAI();
        age = 0;
    }

    /**
     * constructor for RabbitImpl, it initialize energy as parameter, ai and age as initial value.
     */
    protected RabbitImpl(int initEnergy) {
        energy = initEnergy;
        ai = new RabbitAI();
        age = 0;
    }

    /**
     * Return rabbit's view range.
     *
     * @return an Integer representing rabbit view range.
     */
    @Override
    public int getViewRange() {
        return RABBIT_VIEW_RANGE;
    }

    /**
     * Return rabbit's cool down.
     *
     * @return an Integer representing rabbit cool down.
     */
    @Override
    public int getCoolDown() {
        return RABBIT_COOL_DOWN;
    }

    /**
     * Return energy value when other object eats rabbit.
     *
     * @return an Integer representing energy value to earn when others eat rabbit.
     */
    @Override
    public int getEnergyValue() {
        return RABBIT_ENERGY_VALUE;
    }

    /**
     * Return rabbit's max energy.
     *
     * @return an Integer representing rabbit max energy.
     */
    @Override
    public int getMaxEnergy() {
        return RABBIT_MAX_ENERGY;
    }

    /**
     * Return rabbit's breed limit.
     *
     * @return an Integer representing rabbit breed limit.
     */
    @Override
    public int getBreedLimit() {
        return RABBIT_BREED_LIMIT;
    }

    /**
     * Make RabbitImpl object.
     *
     * @param initEnergy
     *          initial energy value of child
     * @return an RabbitImpl object with initEnergy.
     */
    @Override
    protected Animal makeChild(int initEnergy) {
        return new RabbitImpl(initEnergy);
    }

    /**
     * Return rabbit's max age.
     *
     * @return an Integer representing rabbit max age.
     */
    @Override
    protected int getMaxAge() {
        return RABBIT_MAX_AGE;
    }
}
