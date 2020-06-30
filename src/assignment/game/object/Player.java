package assignment.game.object;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    protected String mId;
    protected Marker mMarker;
    protected PlayerColorType mColor;
    protected PlayerPositionType mPosition;
    protected ArrayList<Cake> mRemainCakes = new ArrayList<Cake>();
    protected ArrayList<Cake> mUsableCakes = new ArrayList<Cake>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();

    public Player(final String id) {
        mId = id;
    }

    public int getCakeCount(final CakeLayerType cakeLayerType) {
        int count = 0;
        for (var cake : mRemainCakes) {
            if (cake.getLayer() == cakeLayerType) {
                ++count;
            }
        }
        return count;
    }

    public void takeCake(final CakeLayerType cakeLayerType) {
        final int size = mRemainCakes.size();
        for (int i = 0; i < size; ++i) {
            var cake = mRemainCakes.get(i);
            if (cake.getLayer() == cakeLayerType) {
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
            mRemainCakes.add(new Cake(CakeLayerType.ONE, mPosition));
        }
        for (int i = 0; i < 6; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.TWO, mPosition));
        }
        for (int i = 0; i < 4; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.THREE, mPosition));
        }
        for (int i = 0; i < 2; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.FOUR, mPosition));
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
        return mColor;
    }

    public PlayerPositionType getPosition() {
        return mPosition;
    }

    public void setColor(final PlayerColorType color) {
        mColor = color;
    }

    public void setPosition(final PlayerPositionType position) {
        mPosition = position;
    }
}
