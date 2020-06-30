package assignment.game.object;

public enum PlayerColorType {
    RED(0),
    YELLOW(1),
    GREEN(2),
    BLUE(3);

    private int mIndex;

    PlayerColorType(final int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
