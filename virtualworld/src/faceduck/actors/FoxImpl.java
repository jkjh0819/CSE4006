package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Fox;

/**
 * FoxImpl is an implementation of {@link Fox}.
 * Eat, Breed, Move is implemented at {@link AbstractAnimal}
 */
public class FoxImpl extends AbstractAnimal implements Fox {
    private static final int FOX_MAX_ENERGY = 160;
    private static final int FOX_VIEW_RANGE = 5;
    private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
    private static final int FOX_COOL_DOWN = 2;
    private static final int FOX_INITIAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;
    private static final int FOX_MAX_AGE = 100;

    /**
     * zero constructor for FoxImpl, it initialize energy, ai, age as initial value.
     */
    public FoxImpl(){
        energy = FOX_INITIAL_ENERGY;
        ai = new FoxAI();
        age = 0;
    }

    /**
     * constructor for FoxImpl, it initialize energy as parameter, ai and age as initial value.
     */
    protected FoxImpl(int initEnergy) {
        energy = initEnergy;
        ai = new FoxAI();
        age = 0;
    }

    /**
     * Return fox's view range.
     *
     * @return an Integer representing fox view range.
     */
    @Override
    public int getViewRange() {
        return FOX_VIEW_RANGE;
    }

    /**
     * Return fox's cool down.
     *
     * @return an Integer representing fox cool down.
     */
    @Override
    public int getCoolDown() {
        return FOX_COOL_DOWN;
    }

    /**
     * Return fox's max energy
     *
     * @return an Integer representing fox max energy.
     */
    @Override
    public int getMaxEnergy() {
        return FOX_MAX_ENERGY;
    }

    /**
     * Return fox's breed limit.
     *
     * @return an Integer representing fox breed limit.
     */
    @Override
    public int getBreedLimit() {
        return FOX_BREED_LIMIT;
    }

    /**
     * Make FoxImpl object.
     *
     * @param initEnergy
     *          initial energy value of child
     * @return an FoxImpl object with initEnergy.
     */
    @Override
    protected Animal makeChild(int initEnergy) {
        return new FoxImpl(energy);
    }

    /**
     * Return fox's max age.
     *
     * @return an Integer representing fox max age.
     */
    @Override
    protected int getMaxAge(){
        return FOX_MAX_AGE;
    }
}
