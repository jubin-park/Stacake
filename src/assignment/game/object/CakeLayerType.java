package assignment.game.object;

public enum CakeLayerType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    public static final int SIZE = values().length;

    private int mValue;

    CakeLayerType(final int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
