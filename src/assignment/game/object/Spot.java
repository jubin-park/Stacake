package assignment.game.object;

import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public final class Spot {
    private static final int IMAGE_WIDTH = 49;
    private static final int IMAGE_HEIGHT = 49;

    private BufferedImage mSubImage;
    private ArrayList<Building> mBuildings = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel[] mLabelStories = new JLabel[BuildingLayerType.values().length];

    public Spot() {
        mSubImage = ResourceManager.getInstance().getImageSetSpot().getSubimage(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        mLabelSpot = new JLabel(new ImageIcon(mSubImage));
        for (int i = 0; i < mLabelStories.length; ++i) {
            mLabelStories[i] = new JLabel(StringUtility.EMPTY + i);
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
