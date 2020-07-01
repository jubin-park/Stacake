package assignment.game.object;

import assignment.Config;
import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public final class Spot {
    private ArrayList<Cake> mCakes = new ArrayList<>();
    private JLabel mLabelSpot;
    private JLabel mLabelTarget;
    private JLabel[] mLabelTotalLayers = new JLabel[PlayerPositionType.values().length];

    public Spot() {
        mLabelSpot = new JLabel(ResourceManager.getInstance().getImageIconDefaultSpot());
        mLabelSpot.setOpaque(false);

        for (int i = 0; i < mLabelTotalLayers.length; ++i) {
            mLabelTotalLayers[i] = new JLabel();
            mLabelTotalLayers[i].setOpaque(false);
        }

        mLabelTarget = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageTarget()));
        mLabelTarget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mLabelTarget.setOpaque(false);
        mLabelTarget.setVisible(false);
    }

    public void updateLabels() {
        for (var positionType : PlayerPositionType.values()) {
            int index = positionType.getIndex();
            int count = getCakeLayerCount(positionType);

            if (count > 0) {
                mLabelTotalLayers[index].setText(StringUtility.EMPTY + count);
            } else {
                mLabelTotalLayers[index].setText(StringUtility.EMPTY);
            }
        }
    }

    public void updateSpotColor(final Player player) {
        if (getCakeLayerCount(player.getPosition()) <= 0) {
            mLabelSpot.setIcon(ResourceManager.getInstance().getImageIconDefaultSpot());
            return;
        }
        int x = player.getPosition().getIndex()  * Config.SPOT_IMAGE_WIDTH;
        int y = (player.getColor().getIndex() + 1) * Config.SPOT_IMAGE_HEIGHT;
        var subImage = ResourceManager.getInstance().getImageSetSpot().getSubimage(x, y, Config.SPOT_IMAGE_WIDTH, Config.SPOT_IMAGE_HEIGHT);
        mLabelSpot.setIcon(new ImageIcon(subImage));
    }

    public void stackCake(final Cake cake) {
        mCakes.add(cake);
    }

    public int getCakeLayerCount(final PlayerPositionType playerPositionType) {
        int count = 0;
        for (var cake : mCakes) {
            if (cake.getPlayerPosition() == playerPositionType) {
                count += cake.getLayerType().getValue();
            }
        }

        return count;
    }

    public boolean isStackable(final Cake targetCake) {
        int targetLayerCount = 0;
        int maxLayerCount = 0;
        for (var positionType : PlayerPositionType.values()) {
            int count = getCakeLayerCount(positionType);
            if (positionType == targetCake.getPlayerPosition()) {
                targetLayerCount = count;

                continue;
            }

            if (maxLayerCount < count) {
                maxLayerCount = count;
            }
        }

        return targetLayerCount + targetCake.getLayerType().getValue() >= maxLayerCount;
    }

    public void addToJLayeredPane(final JLayeredPane layeredPane) {
        layeredPane.add(mLabelTarget);
        for (var label : mLabelTotalLayers) {
            layeredPane.add(label);
        }
        layeredPane.add(mLabelSpot);
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

    public void setTargetVisible(final boolean value) {
        mLabelTarget.setVisible(value);
    }
}
