package assignment.game.object;

import java.util.ArrayList;
import java.util.Random;

public final class NetPlayer extends Player {
    public NetPlayer(final String id) {
        super(id);
    }

    @Override
    public void takeOutCardFromDummy(final ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        CardType selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);
    }

    @Override
    public void pickUpCard(final int index) {
        mCardInHand = mCards.get(index);
        mCards.remove(index);
    }

    @Override
    public void useCake(final int index) {
        mCardInHand = null;
        mUsableCakes.remove(index);
    }
}
