package assignment.game.object;

import assignment.utility.ImageUtility;
import assignment.utility.ResourceManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Player {
    protected static final int CARD_IMAGE_WIDTH = 64;
    protected static final int CARD_IMAGE_HEIGHT = 64;

    protected String mId;
    protected Marker mMarker;
    protected PlayerColorType mColor;
    protected PlayerPositionType mPosition;
    protected ArrayList<Cake> mRemainCakes = new ArrayList<Cake>();
    protected ArrayList<Cake> mUsableCakes = new ArrayList<Cake>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();
    protected DefaultListModel<ImageIcon> mModelCardImages = new DefaultListModel<ImageIcon>();

    public Player(final String id) {
        mId = id;

        for (int i = 0; i < 2; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.ONE, mPosition));
        }
        for (int i = 0; i < 4; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.TWO, mPosition));
        }
        for (int i = 0; i < 6; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.THREE, mPosition));
        }
        for (int i = 0; i < 12; ++i) {
            mRemainCakes.add(new Cake(CakeLayerType.FOUR, mPosition));
        }
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

    public void takeCardFromDummy(ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        var selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCard().getSubimage(CARD_IMAGE_HEIGHT * selectedCard.getIndex(), 0, CARD_IMAGE_WIDTH, CARD_IMAGE_HEIGHT);
        int degree = 90 * mPosition.getIndex();
        mModelCardImages.addElement(new ImageIcon(ImageUtility.rotateImageClockwise(subImage, degree)));
    }

    public void discardCardByIndex(int index) {
        mCards.remove(index);
        mModelCardImages.remove(index);
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

    public PlayerColorType getColor() {
        return mColor;
    }

    public PlayerPositionType getPosition() {
        return mPosition;
    }

    public void setColor(PlayerColorType color) {
        mColor = color;
    }

    public void setPosition(PlayerPositionType position) {
        mPosition = position;
    }

    public DefaultListModel<ImageIcon> getModelCardImages() {
        return mModelCardImages;
    }
}
