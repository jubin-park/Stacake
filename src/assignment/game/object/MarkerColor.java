package assignment.game.object;

public enum MarkerColor {
    RED(0),
    YELLOW(1),
    GREEN(2),
    BLUE(3);

    private int mIndex;

    MarkerColor(final int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
