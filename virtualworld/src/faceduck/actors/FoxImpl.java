package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Fox;

public class FoxImpl extends AbstractAnimal implements Fox {
    private static final int FOX_MAX_ENERGY = 160;
    private static final int FOX_VIEW_RANGE = 5;
    private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
    private static final int FOX_COOL_DOWN = 2;
    private static final int FOX_INITIAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;
    private static final int FOX_MAX_AGE = 100;

    public FoxImpl(){
        energy = FOX_INITIAL_ENERGY;
        ai = new FoxAI();
        age = 0;
    }

    protected FoxImpl(int initEnergy) {
        energy = initEnergy;
        ai = new FoxAI();
        age = 0;
    }

    @Override
    public int getViewRange() {
        return FOX_VIEW_RANGE;
    }

    @Override
    public int getCoolDown() {
        return FOX_COOL_DOWN;
    }

    @Override
    public int getMaxEnergy() {
        return FOX_MAX_ENERGY;
    }

    @Override
    public int getBreedLimit() {
        return FOX_BREED_LIMIT;
    }

    @Override
    protected Animal makeChild(int initEnergy) {
        return new FoxImpl(energy);
    }

    @Override
    protected int getMaxAge(){
        return FOX_MAX_AGE;
    }
}
