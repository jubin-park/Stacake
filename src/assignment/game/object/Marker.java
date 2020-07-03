package assignment.game.object;

import assignment.Config;
import assignment.utility.ResourceManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public final class Marker {
    private Player mPlayer;
    private BufferedImage mSubImage;
    private JLayeredPane mLayeredPane;
    private JLabel mLabelMarker;
    private JLabel mLabelId;

    public Marker(final Player player) {
        mPlayer = player;

        mSubImage = ResourceManager.getInstance().getImageSetMarker().getSubimage(mPlayer.getPosition().getIndex() * Config.MARKER_IMAGE_WIDTH, mPlayer.getColor().getIndex() * Config.MARKER_IMAGE_HEIGHT, Config.MARKER_IMAGE_WIDTH, Config.MARKER_IMAGE_HEIGHT);

        mLayeredPane = new JLayeredPane();
        mLayeredPane.setPreferredSize(new Dimension(Config.MARKER_PANE_WIDTH, Config.MARKER_PANE_HEIGHT));

        mLabelMarker = new JLabel(new ImageIcon(mSubImage));
        mLabelMarker.setBounds(0, 0, Config.MARKER_PANE_WIDTH, Config.MARKER_PANE_HEIGHT);
        mLabelMarker.setHorizontalAlignment(SwingConstants.CENTER);

        mLabelId = new JLabel(mPlayer.getId());
        mLabelId.setBounds(0, 0, Config.MARKER_PANE_WIDTH, Config.MARKER_PANE_HEIGHT);
        mLabelId.setHorizontalAlignment(SwingConstants.CENTER);
        mLabelId.setVerticalAlignment(SwingConstants.BOTTOM);

        mLayeredPane.add(mLabelId);
        mLayeredPane.add(mLabelMarker);
    }

    public JLayeredPane getLayeredPane() {
        return mLayeredPane;
    }
}
