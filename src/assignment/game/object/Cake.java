package assignment.game.object;

public final class Cake {
    private final CakeLayerType mLayer;
    private final PlayerPositionType mPlayerPosition;

    public Cake(final CakeLayerType layer, final PlayerPositionType playerPosition) {
        mLayer = layer;
        mPlayerPosition = playerPosition;
    }

    public CakeLayerType getLayer() {
        return mLayer;
    }

    public PlayerPositionType getPlayerPosition() {
        return mPlayerPosition;
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
        return mLayer == otherCake.mLayer && mPlayerPosition == otherCake.mPlayerPosition;
    }
}
