package assignment.game.object;

public enum CakeLayerType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int mValue;

    CakeLayerType(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
