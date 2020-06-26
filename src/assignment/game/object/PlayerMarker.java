package assignment.game.object;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class PlayerMarker {
    private static final String IMG_PATH = "resources/images/target.png";

    private JLayeredPane mLayeredPane;
    private JLabel mLabelMarker;
    private JLabel mLabelId;
    private PlayerColorType mType;

    public PlayerMarker(final String playerId, final PlayerColorType type) {
        mLayeredPane = new JLayeredPane();
        mLayeredPane.setPreferredSize(new Dimension(64, 64));

        mLabelMarker = new JLabel(new ImageIcon(IMG_PATH));
        mLabelMarker.setBounds(10, 0, 49, 49);
        mLabelMarker.setHorizontalAlignment(SwingConstants.CENTER);

        mLabelId = new JLabel(playerId);
        mLabelId.setBounds(10, 0, 49, 49);

        mLayeredPane.add(mLabelMarker, 0);
        mLayeredPane.add(mLabelId, 1);

        mType = type;
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

    public PlayerColorType getType() {
        return mType;
    }
}
