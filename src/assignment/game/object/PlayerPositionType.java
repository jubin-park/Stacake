package assignment.game.object;

public enum PlayerPositionType {
    SOUTH(0),
    WEST(1),
    NORTH(2),
    EAST(3);

    private int mIndex;

    PlayerPositionType(final int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
