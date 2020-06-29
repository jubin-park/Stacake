package assignment.game.object;

public enum BuildingStory {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int mValue;

    BuildingStory(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
