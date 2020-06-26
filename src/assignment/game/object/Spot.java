package assignment.game.object;

import assignment.Program;
import assignment.utility.StringUtility;
import assignment.window.MainWindow;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class Spot {
    private static final String IMG_PATH = "resources/images/spots.png";
    private static BufferedImage mImage;

    private BufferedImage mSubImage;
    private ArrayList<Building> mBuildings = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel[] mLabelStories = new JLabel[4];

    public Spot() {
        loadImage();
        mLabelSpot = new JLabel(new ImageIcon(mSubImage));
        for (int i = 0; i < mLabelStories.length; ++i) {
            mLabelStories[i] = new JLabel(StringUtility.EMPTY + i);
        }
    }

    private void loadImage() {
        if (mImage == null) {
            try {
                //URL urlToImage = Program.class.getResource(IMG_PATH);
                // InputStream imgStream = Program.class.getResourceAsStream(IMG_PATH);
                mImage = ImageIO.read(Program.class.getResourceAsStream(IMG_PATH));
            } catch (IOException ex) {

            }
        }
        mSubImage = mImage.getSubimage(49, 49, 49, 49);
    }

    public void stackBuilding(final Building building) {
        mBuildings.add(building);
    }

    public int getBuildingStoryCount(final PlayerColorType type) {
        int count = 0;

        for (var building : mBuildings) {
            if (building.getType() == type) {
                count += building.getStory().getValue();
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
