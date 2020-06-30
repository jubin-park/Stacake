package assignment.game.object;

import assignment.Program;
import assignment.utility.ResourceManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class Marker {
    private static final int PANE_WIDTH = 64;
    private static final int PANE_HEIGHT = 72;
    private static final int IMAGE_WIDTH = 49;
    private static final int IMAGE_HEIGHT = 49;

    private BufferedImage mSubImage;
    private JLayeredPane mLayeredPane;
    private JLabel mLabelMarker;
    private JLabel mLabelId;
    private Player mPlayer;

    public Marker(final Player player) {
        mPlayer = player;
        mSubImage = ResourceManager.getInstance().getImageSetMarker().getSubimage(mPlayer.getPosition().getIndex() * IMAGE_WIDTH, mPlayer.getColor().getIndex() * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        mLayeredPane = new JLayeredPane();
        mLayeredPane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));

        mLabelMarker = new JLabel(new ImageIcon(mSubImage));
        mLabelMarker.setBounds(0, 0, PANE_WIDTH, PANE_HEIGHT);
        mLabelMarker.setHorizontalAlignment(SwingConstants.CENTER);

        mLabelId = new JLabel(mPlayer.getId());
        mLabelId.setBounds(0, 0, PANE_WIDTH, PANE_HEIGHT);
        mLabelId.setHorizontalAlignment(SwingConstants.CENTER);
        mLabelId.setVerticalAlignment(SwingConstants.BOTTOM);

        mLayeredPane.add(mLabelId);
        mLayeredPane.add(mLabelMarker);
        //mLayeredPane.setComponentZOrder(mLabelId, 0);
    }

    public JLayeredPane getLayeredPane() {
        return mLayeredPane;
    }

    public JLabel getLabelMarker() {
        return mLabelMarker;
    }

    public JLabel getLabelId() {
        return mLabelId;
    }
}
