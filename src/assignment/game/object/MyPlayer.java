package assignment.game.object;

import assignment.Config;
import assignment.utility.ImageUtility;
import assignment.utility.ResourceManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public final class MyPlayer extends Player {
    private boolean mbCardSelected;
    private boolean mbCakeSelected;
    private CardType mNowCard;
    private DefaultListModel<ImageIcon> mModelCardImages = new DefaultListModel<ImageIcon>();
    private DefaultListModel<ImageIcon> mModelUsableCakeImages = new DefaultListModel<ImageIcon>();

    public MyPlayer(String id) {
        super(id);
    }

    public boolean isCardSelected() {
        return mbCardSelected;
    }

    public boolean isCakeSelected() {
        return mbCakeSelected;
    }

    public CardType getNowCard() {
        return mNowCard;
    }

    public DefaultListModel<ImageIcon> getModelCardImages() {
        return mModelCardImages;
    }

    public DefaultListModel<ImageIcon> getModelUsableCakeImages() {
        return mModelUsableCakeImages;
    }

    public void setCardSelected(boolean bCardSelected) {
        mbCardSelected = bCardSelected;
    }

    public void setCakeSelected(boolean bCakeSelected) {
        mbCakeSelected = bCakeSelected;
    }

    public void setNowCard(final CardType nowCard) {
        mNowCard = nowCard;
    }

    @Override
    public void takeCardFromDummy(final ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        var selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCard().getSubimage(Config.CARD_IMAGE_HEIGHT * selectedCard.getIndex(), 0, Config.CARD_IMAGE_WIDTH, Config.CARD_IMAGE_HEIGHT);
        int degree = 90 * mPositionType.getIndex();
        mModelCardImages.addElement(new ImageIcon(ImageUtility.rotateImageClockwise(subImage, degree)));
    }

    @Override
    public void pickUpCard(final CardType card) {
        final int index = mCards.indexOf(card);
        mCards.remove(index);
        mModelCardImages.remove(index);
    }

    public CardType pickUpRandomCard() {
        assert (!mCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(mCards.size());
        var selectedCard = mCards.get(selectedIndex);

        mCards.remove(selectedCard);
        mModelCardImages.remove(selectedIndex);

        return selectedCard;
    }

    @Override
    public void takeCake(final CakeLayerType cakeLayerType) {
        final int size = mRemainCakes.size();
        for (int i = 0; i < size; ++i) {
            var cake = mRemainCakes.get(i);

            if (cake.getLayerType() == cakeLayerType) {
                mRemainCakes.remove(i);
                mUsableCakes.add(cake);
                BufferedImage subImage = ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * (cakeLayerType.getValue() - 1), 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT);
                mModelUsableCakeImages.addElement(new ImageIcon(subImage));

                return;
            }
        }
    }

    public void takeRandomCake() {
        assert (!mRemainCakes.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(mRemainCakes.size());
        var selectedCake = mRemainCakes.get(selectedIndex);

        mRemainCakes.remove(selectedIndex);
        mUsableCakes.add(selectedCake);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * (selectedCake.getLayerType().getValue() - 1), 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT);
        mModelUsableCakeImages.addElement(new ImageIcon(subImage));
    }


    @Override
    public void useCake(final Cake cake) {
        final int index = mUsableCakes.indexOf(cake);
        mUsableCakes.remove(index);
        mModelUsableCakeImages.remove(index);
    }
}
