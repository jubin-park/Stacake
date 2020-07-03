package assignment.game.object;

import assignment.Config;
import assignment.utility.ImageUtility;
import assignment.utility.ResourceManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public final class MyPlayer extends Player {
    private DefaultListModel<ImageIcon> mModelCardImages = new DefaultListModel<ImageIcon>();
    private DefaultListModel<ImageIcon> mModelUsableCakeImages = new DefaultListModel<ImageIcon>();
    private boolean mbCardSelected;
    private boolean mbCakeSelected;

    public MyPlayer(String id) {
        super(id);
    }

    public DefaultListModel<ImageIcon> getModelCardImages() {
        return mModelCardImages;
    }

    public DefaultListModel<ImageIcon> getModelUsableCakeImages() {
        return mModelUsableCakeImages;
    }

    public boolean isCardSelected() {
        return mbCardSelected;
    }

    public void setCardSelected(boolean bCardSelected) {
        mbCardSelected = bCardSelected;
    }

    public boolean isCakeSelected() {
        return mbCakeSelected;
    }

    public void setCakeSelected(boolean bCakeSelected) {
        mbCakeSelected = bCakeSelected;
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
        assert (false);
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
    public void useCard(final CardType card) {
        final int index = mCards.indexOf(card);
        mCards.remove(index);
        mModelCardImages.remove(index);
    }

    public void useCake(final Cake cake) {
        final int index = mUsableCakes.indexOf(cake);
        mUsableCakes.remove(index);
        mModelUsableCakeImages.remove(index);
    }
}
