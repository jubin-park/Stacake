package assignment.game.object;

public enum CardType {
    RC00(0),
    RC01(1),
    RC02(2),
    RC10(3),
    RC11(4),
    RC12(5),
    RC20(6),
    RC21(7),
    RC22(8);

    private int mIndex;

    CardType(final int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
