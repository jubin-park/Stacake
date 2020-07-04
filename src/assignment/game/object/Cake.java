package assignment.game.object;

public final class Cake {
    private final CakeLayerType mLayerType;
    private final Player mOwner;

    public Cake(final CakeLayerType layerType, final Player owner) {
        mLayerType = layerType;
        mOwner = owner;
    }

    public CakeLayerType getLayerType() {
        return mLayerType;
    }

    public Player getOwner() {
        return mOwner;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Cake)) {
            return false;
        }

        Cake otherCake = (Cake) obj;
        return mLayerType == otherCake.mLayerType && mOwner == otherCake.mOwner;
    }
}
