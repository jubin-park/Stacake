package assignment.game.object;

import assignment.Config;
import assignment.frame.FrameMain;
import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public final class Spot {
    private BufferedImage mSubImage;
    private ArrayList<Cake> mCakes = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel mLabelTarget;
    private JLabel[] mLabelTotalLayers = new JLabel[PlayerPositionType.values().length];

    public Spot() {
        mSubImage = ResourceManager.getInstance().getImageSetSpot().getSubimage(0, 0, Config.SPOT_IMAGE_WIDTH, Config.SPOT_IMAGE_HEIGHT);
        mLabelSpot = new JLabel(new ImageIcon(mSubImage));
        mLabelSpot.setOpaque(false);

        for (int i = 0; i < mLabelTotalLayers.length; ++i) {
            mLabelTotalLayers[i] = new JLabel();
            //mLabelTotalLayers[i].setBackground(Color.ORANGE);
            mLabelTotalLayers[i].setOpaque(false);
        }

        mLabelTarget = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageTarget()));

        mLabelTarget.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                FrameMain.getInstance().setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                FrameMain.getInstance().setCursor(Cursor.getDefaultCursor());
            }
        });

        mLabelTarget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mLabelTarget.setOpaque(false);
        mLabelTarget.setVisible(false);
    }

    public void setTargetVisible(final boolean value) {
        mLabelTarget.setVisible(value);
    }

    public void updateLabel() {
        for (var position : PlayerPositionType.values()) {
            int index = position.getIndex();
            int count = getCakeLayerCount(position);

            if (count > 0) {
                mLabelTotalLayers[index].setText(StringUtility.EMPTY + count);
            } else {
                mLabelTotalLayers[index].setText(StringUtility.EMPTY);
            }
        }
    }

    public JLabel getLabelSpot() {
        return mLabelSpot;
    }

    public JLabel getLabelTarget() {
        return mLabelTarget;
    }

    public JLabel[] getLabelTotalLayers() {
        return mLabelTotalLayers;
    }

    public void stackCake(final Cake cake) {
        mCakes.add(cake);
    }

    public int getCakeLayerCount(final PlayerPositionType playerPosition) {
        int count = 0;
        for (var cake : mCakes) {
            if (cake.getPlayerPosition() == playerPosition) {
                count += cake.getLayer().getValue();
            }
        }

        return count;
    }

    public void addTo(final JLayeredPane layeredPane) {
        layeredPane.add(mLabelSpot);
        for (int i = 0; i < mLabelTotalLayers.length; ++i) {
            layeredPane.add(mLabelTotalLayers[i]);
        }
        layeredPane.add(mLabelTarget);
    }
}
