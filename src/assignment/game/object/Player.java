package assignment.game.object;

import assignment.Program;
import assignment.utility.ImageUtility;
import assignment.utility.ResourceManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Player {
    protected static final int CARD_IMAGE_WIDTH = 64;
    protected static final int CARD_IMAGE_HEIGHT = 64;

    protected String mId;
    protected Marker mMarker;
    protected ArrayList<BuildingLayerType> mRemainBuildings = new ArrayList<BuildingLayerType>();
    protected ArrayList<BuildingLayerType> mUsableBuildings = new ArrayList<BuildingLayerType>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();
    protected DefaultListModel<ImageIcon> mModelCardImages = new DefaultListModel<ImageIcon>();

    public Player(String id) {
        mId = id;
        for (int i = 0; i < 2; ++i) {
            mRemainBuildings.add(BuildingLayerType.ONE);
        }
        for (int i = 0; i < 4; ++i) {
            mRemainBuildings.add(BuildingLayerType.TWO);
        }
        for (int i = 0; i < 6; ++i) {
            mRemainBuildings.add(BuildingLayerType.THREE);
        }
        for (int i = 0; i < 12; ++i) {
            mRemainBuildings.add(BuildingLayerType.FOUR);
        }
    }

    public int getBuildingCount(final BuildingLayerType buildingLayerType) {
        int count = 0;
        for (var story : mRemainBuildings) {
            if (story == buildingLayerType) {
                ++count;
            }
        }
        return count;
    }

    public void takeBuilding(final BuildingLayerType buildingLayerType) {
        if (mRemainBuildings.remove(buildingLayerType)) {
            mUsableBuildings.add(buildingLayerType);
        } else {
            assert (false);
        }
    }

    public void takeCardFromDummy(ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int selectedIndex = random.nextInt(dummyCards.size());
        var selectedCard = dummyCards.get(selectedIndex);

        dummyCards.remove(selectedIndex);
        mCards.add(selectedCard);

        BufferedImage subImage = ResourceManager.getInstance().getImageSetCard().getSubimage(CARD_IMAGE_HEIGHT * selectedCard.getIndex(), 0, CARD_IMAGE_WIDTH, CARD_IMAGE_HEIGHT);
        int degree = 90 * mMarker.getPosition().getIndex();
        mModelCardImages.addElement(new ImageIcon(ImageUtility.rotateImageClockwise(subImage, degree)));
    }

    public void discardCardByIndex(int index) {
        mCards.remove(index);
        mModelCardImages.remove(index);
    }

    public String getId() {
        return mId;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }

    public ArrayList<CardType> getCards() {
        return mCards;
    }

    public DefaultListModel<ImageIcon> getModelCardImages() {
        return mModelCardImages;
    }
}
