package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.interfaces.Rabbit;

public class FoxImpl extends AbstractAnimal implements Fox {
    private static final int FOX_MAX_ENERGY = 160;
    private static final int FOX_VIEW_RANGE = 5;
    private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
    private static final int FOX_COOL_DOWN = 2;
    private static final int FOX_INITAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;

    public FoxImpl(){
        energy = FOX_INITAL_ENERGY;
        ai = new FoxAI();
    }

    protected FoxImpl(int initEnergy) {
        energy = initEnergy;
        ai = new FoxAI();
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
    public Animal makeChild(int initEnergy) {
        return new FoxImpl(energy);
    }
}
