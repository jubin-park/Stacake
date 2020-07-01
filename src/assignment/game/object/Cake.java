package assignment.game.object;

public final class Cake {
    private final CakeLayerType mLayerType;
    private final PlayerPositionType mPlayerPositionType;

    public Cake(final CakeLayerType layerType, final PlayerPositionType playerPositionType) {
        mLayerType = layerType;
        mPlayerPositionType = playerPositionType;
    }

    public CakeLayerType getLayerType() {
        return mLayerType;
    }

    public PlayerPositionType getPlayerPosition() {
        return mPlayerPositionType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Cake)) {
            return false;
        }

        Cake otherCake = (Cake) obj;
        return mLayerType == otherCake.mLayerType && mPlayerPositionType == otherCake.mPlayerPositionType;
    }
}
