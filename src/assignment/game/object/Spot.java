package assignment.game.object;

import assignment.Program;
import assignment.utility.StringUtility;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class Spot {
    private static final String IMG_PATH = "resources/images/spots.png";
    private static BufferedImage mImage;
    private static final int IMAGE_WIDTH = 49;
    private static final int IMAGE_HEIGHT = 49;

    private BufferedImage mSubImage;
    private ArrayList<Building> mBuildings = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel[] mLabelStories = new JLabel[BuildingLayerType.values().length];

    public Spot() {
        loadImage();
        mSubImage = mImage.getSubimage(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        mLabelSpot = new JLabel(new ImageIcon(mSubImage));
        for (int i = 0; i < mLabelStories.length; ++i) {
            mLabelStories[i] = new JLabel(StringUtility.EMPTY + i);
        }
    }

    private void loadImage() {
        if (mImage == null) {
            try {
                mImage = ImageIO.read(Program.class.getResourceAsStream(IMG_PATH));
            } catch (IOException ex) {

            }
        }
    }

    public void stackBuilding(final Building building) {
        mBuildings.add(building);
    }

    public int getBuildingStoryCount(final PlayerPositionType position) {
        int count = 0;

        for (var building : mBuildings) {
            if (building.getPosition() == position) {
                count += building.getLayer().getValue();
            }
        }

        return count;
    }

    public JLabel getLabelSpot() {
        return mLabelSpot;
    }

    public void addTo(JComponent component) {
        component.add(mLabelSpot);
        for (int i = 0; i < mLabelStories.length; ++i) {
            component.add(mLabelStories[i]);
        }
    }
}
