package assignment.game.object;

import assignment.Config;
import assignment.utility.ImageUtility;
import assignment.utility.ResourceManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public final class MyPlayer extends Player {
    private boolean mbCakeSelected;
    private DefaultListModel<ImageIcon> mModelCardImages = new DefaultListModel<ImageIcon>();
    private DefaultListModel<ImageIcon> mModelUsableCakeImages = new DefaultListModel<ImageIcon>();

    public MyPlayer(String id) {
        super(id);
    }

    public boolean isCardSelected() {
        return mCardInHand != null;
    }

    public boolean isCakeSelected() {
        return mbCakeSelected;
    }

    public DefaultListModel<ImageIcon> getModelCardImages() {
        return mModelCardImages;
    }

    public DefaultListModel<ImageIcon> getModelUsableCakeImages() {
        return mModelUsableCakeImages;
    }

    public void setCakeSelected(boolean bCakeSelected) {
        mbCakeSelected = bCakeSelected;
    }

    @Override
    public void takeOutCardFromDummy(final ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        CardType selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCard().getSubimage(Config.CARD_IMAGE_HEIGHT * selectedCard.getIndex(), 0, Config.CARD_IMAGE_WIDTH, Config.CARD_IMAGE_HEIGHT);
        int degree = 90 * mPositionType.getIndex();
        mModelCardImages.addElement(new ImageIcon(ImageUtility.rotateImageClockwise(subImage, degree)));
    }

    @Override
    public void pickUpCard(final int index) {
        mCardInHand = mCards.get(index);
        mCards.remove(index);
        mModelCardImages.remove(index);
    }

    @Override
    public void takeOutCakeFromFridge(final CakeLayerType cakeLayerType) {
        final int size = mFridgeCakes.size();
        for (int i = 0; i < size; ++i) {
            Cake cake = mFridgeCakes.get(i);

            if (cake.getLayerType() == cakeLayerType) {
                mFridgeCakes.remove(i);
                mUsableCakes.add(cake);
                BufferedImage subImage = ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * (cakeLayerType.getValue() - 1), 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT);
                mModelUsableCakeImages.addElement(new ImageIcon(subImage));

                return;
            }
        }
    }

    @Override
    public void takeOutCakeFromFridgeByIndex(final int index) {
        Cake cake = mFridgeCakes.get(index);
        mFridgeCakes.remove(cake);
        mUsableCakes.add(cake);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * (cake.getLayerType().getValue() - 1), 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT);
        mModelUsableCakeImages.addElement(new ImageIcon(subImage));
    }

    @Override
    public void useCake(final int index) {
        mCardInHand = null;
        mbCakeSelected = true;
        mUsableCakes.remove(index);
        mModelUsableCakeImages.remove(index);
    }

    public void initState() {
        mCardInHand = null;
        mbCakeSelected = false;
    }

    public void retrieveCard() {
        assert (mCardInHand != null);
        mCards.add(mCardInHand);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCard().getSubimage(Config.CARD_IMAGE_HEIGHT * mCardInHand.getIndex(), 0, Config.CARD_IMAGE_WIDTH, Config.CARD_IMAGE_HEIGHT);
        int degree = 90 * mPositionType.getIndex();
        mModelCardImages.addElement(new ImageIcon(ImageUtility.rotateImageClockwise(subImage, degree)));

        mCardInHand = null;
    }
}
