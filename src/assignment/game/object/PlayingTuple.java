package assignment.game.object;

public final class PlayingTuple {
    private int mCardIndex;
    private Spot mSpot;
    private int mCakeIndex;

    public PlayingTuple(final int cardIndex, final Spot spot, final int cakeIndex) {
        mCardIndex = cardIndex;
        mSpot = spot;
        mCakeIndex = cakeIndex;
    }

    public int getCardIndex() {
        return mCardIndex;
    }

    public Spot getSpot() {
        return mSpot;
    }

    public int getCakeIndex() {
        return mCakeIndex;
    }
}
