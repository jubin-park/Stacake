package assignment.game.object;

public enum MarkerPosition {
    SOUTH(0),
    WEST(1),
    NORTH(2),
    EAST(3);

    private int mIndex;

    MarkerPosition(final int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
