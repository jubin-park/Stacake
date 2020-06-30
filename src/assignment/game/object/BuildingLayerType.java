package assignment.game.object;

public enum BuildingLayerType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int mValue;

    BuildingLayerType(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
