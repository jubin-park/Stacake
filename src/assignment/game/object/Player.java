package assignment.game.object;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    protected String mId;
    protected Marker mMarker;
    protected PlayerColorType mColorType;
    protected PlayerPositionType mPositionType;
    protected boolean mbCakeSelectingFinished;

    protected ArrayList<Cake> mRemainCakes = new ArrayList<Cake>();
    protected ArrayList<Cake> mUsableCakes = new ArrayList<Cake>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();

    protected Player(final String id) {
        mId = id;
    }

    public int getCakeCount(final CakeLayerType cakeLayerType) {
        int count = 0;
        for (var cake : mRemainCakes) {
            if (cake.getLayerType() == cakeLayerType) {
                ++count;
            }
        }
        return count;
    }

    public void takeCake(final CakeLayerType cakeLayerType) {
        final int size = mRemainCakes.size();
        for (int i = 0; i < size; ++i) {
            var cake = mRemainCakes.get(i);
            if (cake.getLayerType() == cakeLayerType) {
                mRemainCakes.remove(i);
                mUsableCakes.add(cake);

                return;
            }
        }
        assert (false);
    }

    public void takeCardFromDummy(final ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        var selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);
    }

    public void useCard(final CardType card) {
        mCards.remove(card);
    }

    public void useCake(final Cake cake) {
        mUsableCakes.remove(cake);
    }

    public void initializeCakes() {
        for (int i = 0; i < 12; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.ONE, mPositionType));
        }
        for (int i = 0; i < 6; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.TWO, mPositionType));
        }
        for (int i = 0; i < 4; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.THREE, mPositionType));
        }
        for (int i = 0; i < 2; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.FOUR, mPositionType));
        }
    }

    public void createMarker() {
        mMarker = new Marker(this);
    }

    public String getId() {
        return mId;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public ArrayList<CardType> getCards() {
        return mCards;
    }

    public ArrayList<Cake> getUsableCakes() {
        return mUsableCakes;
    }

    public PlayerColorType getColor() {
        return mColorType;
    }

    public PlayerPositionType getPosition() {
        return mPositionType;
    }

    public boolean isCakeSelectingFinished() {
        return mbCakeSelectingFinished;
    }

    public void setCakeSelectingFinished(final boolean value) {
        mbCakeSelectingFinished = value;
    }

    public void setColor(final PlayerColorType colorType) {
        mColorType = colorType;
    }

    public void setPosition(final PlayerPositionType positionType) {
        mPositionType = positionType;
    }
}
