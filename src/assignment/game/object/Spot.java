package assignment.game.object;

import assignment.Config;
import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public final class Spot {
    private BufferedImage mSubImage;
    private ArrayList<Cake> mCakes = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel[] mLabelStories = new JLabel[CakeLayerType.values().length];

    public Spot() {
        mSubImage = ResourceManager.getInstance().getImageSetSpot().getSubimage(0, 0, Config.SPOT_IMAGE_WIDTH, Config.SPOT_IMAGE_HEIGHT);

        mLabelSpot = new JLabel(new ImageIcon(mSubImage));
        for (int i = 0; i < mLabelStories.length; ++i) {
            mLabelStories[i] = new JLabel(StringUtility.EMPTY + i);
        }
    }

    public void stackCake(final Cake cake) {
        mCakes.add(cake);
    }

    public int getCakeStoryCount(final PlayerPositionType position) {
        int count = 0;

        for (var cake : mCakes) {
            if (cake.getPlayerPosition() == position) {
                count += cake.getLayer().getValue();
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
