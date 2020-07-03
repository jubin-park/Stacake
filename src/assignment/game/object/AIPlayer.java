package assignment.game.object;

import java.util.Random;

public final class AIPlayer extends Player {
    public AIPlayer(final String id) {
        super(id);
    }

    public void takeRandomCake() {
        assert (!mRemainCakes.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(mRemainCakes.size());
        var selectedCake = mRemainCakes.get(selectedIndex);

        mRemainCakes.remove(selectedIndex);
        mUsableCakes.add(selectedCake);
    }

    public CardType pickUpRandomCard() {
        assert (!mCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(mCards.size());
        var selectedCard = mCards.get(selectedIndex);

        mCards.remove(selectedCard);

        return selectedCard;
    }
}
