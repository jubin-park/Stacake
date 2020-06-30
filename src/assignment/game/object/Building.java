package assignment.game.object;

public final class Building {
    private final PlayerPositionType mPosition;
    private final BuildingLayerType mLayer;

    public Building(final PlayerPositionType position, final BuildingLayerType story) {
        mPosition = position;
        mLayer = story;
    }

    public PlayerPositionType getPosition() {
        return mPosition;
    }

    public BuildingLayerType getLayer() {
        return mLayer;
    }
}
