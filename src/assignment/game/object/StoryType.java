package assignment.game.object;

public enum StoryType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private int mValue;

    StoryType(int value) {
        mValue = value;
    }

    public int getValue() {
        return this.mValue;
    }
}
