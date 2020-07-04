package assignment.game.object;

import assignment.Config;
import java.util.ArrayList;
import java.util.Random;

public abstract class Player {
    protected String mId;
    protected Marker mMarker;
    protected PlayerColorType mColorType;
    protected PlayerPositionType mPositionType;
    protected int mScore;
    protected CardType mCardInHand;
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();
    protected ArrayList<Cake> mUsableCakes = new ArrayList<Cake>();
    protected ArrayList<Cake> mFridgeCakes = new ArrayList<Cake>();

    protected Player(final String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public PlayerColorType getColor() {
        return mColorType;
    }

    public PlayerPositionType getPosition() {
        return mPositionType;
    }

    public int getScore() {
        return mScore;
    }

    public CardType getCardInHand() {
        return mCardInHand;
    }

    public ArrayList<Cake> getUsableCakes() {
        return mUsableCakes;
    }

    public void createMarker() {
        mMarker = new Marker(this);
    }

    public void setColor(final PlayerColorType colorType) {
        mColorType = colorType;
    }

    public void setPosition(final PlayerPositionType positionType) {
        mPositionType = positionType;
    }

    public void addScore(final int score) {
        mScore += score;
    }

    public boolean isCakeSelectingFinished() {
        return mUsableCakes.size() == Config.MAX_SELECTING_CAKE_COUNT;
    }

    public int getCakeLayerCount(final CakeLayerType cakeLayerType) {
        int count = 0;
        for (var cake : mFridgeCakes) {
            if (cake.getLayerType() == cakeLayerType) {
                ++count;
            }
        }
        return count;
    }

    public int getRandomCardIndex() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(mCards.size());
    }

    public int getRandomUsableCakeIndex() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(mUsableCakes.size());
    }

    public int getRandomFridgeCakeIndex() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(mFridgeCakes.size());
    }

    public CardType getCard(int index) {
        return mCards.get(index);
    }

    public Cake getUsableCake(int index) {
        return mUsableCakes.get(index);
    }

    public Cake getFridgeCake(int index) {
        return mFridgeCakes.get(index);
    }

    public void fillUpCakeFridge() {
        for (int i = 0; i < 12; ++i) {
            mFridgeCakes.add(new Cake(CakeLayerType.ONE, this));
        }
        for (int i = 0; i < 6; ++i) {
            mFridgeCakes.add(new Cake(CakeLayerType.TWO, this));
        }
        for (int i = 0; i < 4; ++i) {
            mFridgeCakes.add(new Cake(CakeLayerType.THREE, this));
        }
        for (int i = 0; i < 2; ++i) {
            mFridgeCakes.add(new Cake(CakeLayerType.FOUR, this));
        }
    }

    public void takeOutCakeFromFridge(final CakeLayerType cakeLayerType) {
        final int size = mFridgeCakes.size();
        for (int i = 0; i < size; ++i) {
            var cake = mFridgeCakes.get(i);
            if (cake.getLayerType() == cakeLayerType) {
                mFridgeCakes.remove(i);
                mUsableCakes.add(cake);

                return;
            }
        }
    }

    public void takeOutCakeFromFridgeByIndex(final int index) {
        var cake = mFridgeCakes.get(index);
        mFridgeCakes.remove(cake);
        mUsableCakes.add(cake);
    }

    public abstract void takeOutCardFromDummy(final ArrayList<CardType> dummyCards);

    public abstract void pickUpCard(final int index);

    public abstract void useCake(final int index);

    public PlayingTuple createRandomPlayingTuple(final World world) {
        ArrayList<PlayingTuple> tuples = new ArrayList<PlayingTuple>();

        final int cardSize = mCards.size();
        final int cakeSize = mUsableCakes.size();

        for (var city : world.getCities()) {
            for (int i = 0; i < cardSize; ++i) {
                var spot = city.getSpotByCardAndPositionType(mCards.get(i), mPositionType);
                for (int c = 0; c < cakeSize; ++c) {
                    var cake = mUsableCakes.get(c);
                    if (spot.isStackable(cake)) {
                        tuples.add(new PlayingTuple(i, spot, c));
                    }
                }
            }
        }

        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(tuples.size());

        return tuples.get(index);
    }
}
