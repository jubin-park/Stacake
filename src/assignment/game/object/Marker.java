package assignment.game.object;

import assignment.Program;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class Marker {
    private static final String IMG_PATH = "resources/images/markers.png";
    private static BufferedImage mImage;
    private static final int PANE_WIDTH = 64;
    private static final int PANE_HEIGHT = 72;
    private static final int IMAGE_WIDTH = 49;
    private static final int IMAGE_HEIGHT = 49;

    private BufferedImage mSubImage;
    private JLayeredPane mLayeredPane;
    private JLabel mLabelMarker;
    private JLabel mLabelId;
    private MarkerPosition mPosition;
    private MarkerColor mColor;

    public Marker(final String playerId, final MarkerPosition position, final MarkerColor color, final MarkerPosition standardPosition) {
        loadImage();

        mPosition = position;
        mColor = color;

        int index = mPosition.getIndex() - standardPosition.getIndex();
        while (index < 0) {
            index += 4;
        }

        mSubImage = mImage.getSubimage(index * IMAGE_WIDTH, mColor.getIndex() * IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        mLayeredPane = new JLayeredPane();
        mLayeredPane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));

        mLabelMarker = new JLabel(new ImageIcon(mSubImage));
        mLabelMarker.setBounds(0, 0, PANE_WIDTH, PANE_HEIGHT);
        mLabelMarker.setHorizontalAlignment(SwingConstants.CENTER);

        mLabelId = new JLabel(playerId);
        mLabelId.setBounds(0, 0, PANE_WIDTH, PANE_HEIGHT);
        mLabelId.setHorizontalAlignment(SwingConstants.CENTER);
        mLabelId.setVerticalAlignment(SwingConstants.BOTTOM);

        mLayeredPane.add(mLabelId);
        mLayeredPane.add(mLabelMarker);
        //mLayeredPane.setComponentZOrder(mLabelId, 0);
    }

    private void loadImage() {
        if (mImage == null) {
            try {
                mImage = ImageIO.read(Program.class.getResourceAsStream(IMG_PATH));
            } catch (IOException ex) {

            }
        }
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

    public MarkerColor getColor() {
        return mColor;
    }
}
