package assignment.game.object;

public enum BuildingStoryType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int mValue;

    BuildingStoryType(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
