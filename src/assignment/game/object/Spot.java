package assignment.game.object;

import assignment.Config;
import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public final class Spot {
    private ArrayList<Cake> mCakes = new ArrayList<>();
    private JLabel mLabelSpot = new JLabel(ResourceManager.getInstance().getImageIconDefaultSpot());
    private JLabel mLabelTarget = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageTarget()));
    private JLabel[] mLabelTotalLayers = new JLabel[PlayerPositionType.SIZE];

    public Spot() {
        mLabelSpot.setOpaque(false);

        mLabelTarget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mLabelTarget.setOpaque(false);
        mLabelTarget.setVisible(false);

        for (int i = 0; i < mLabelTotalLayers.length; ++i) {
            mLabelTotalLayers[i] = new JLabel();
            mLabelTotalLayers[i].setOpaque(false);
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

    public int getCakeLayerCount(final PlayerPositionType playerPositionType) {
        int count = 0;
        for (var cake : mCakes) {
            if (cake.getOwner().getPosition() == playerPositionType) {
                count += cake.getLayerType().getValue();
            }
        }

        return count;
    }

    public int getCakeHeight() {
        int count = 0;
        for (var cake : mCakes) {
            count += cake.getLayerType().getValue();
        }

        return count;
    }

    public Player getOwnerOrNull() {
        if (mCakes.isEmpty()) {
            return null;
        }

        return mCakes.get(mCakes.size() - 1).getOwner();
    }

    public void setTargetVisible(final boolean value) {
        mLabelTarget.setVisible(value);
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

    public void clearSpot() {
        mLabelSpot.setIcon(ResourceManager.getInstance().getImageIconDefaultSpot());
    }

    public void updateSpotColor(final Player player) {
        if (getCakeLayerCount(player.getPosition()) <= 0) {
            clearSpot();

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

    public boolean isStackable(final Cake cake) {
        int targetLayerCount = 0;
        int maxLayerCount = 0;

        for (var positionType : PlayerPositionType.values()) {
            int count = getCakeLayerCount(positionType);
            if (positionType == cake.getOwner().getPosition()) {
                targetLayerCount = count;
                continue;
            }

            if (maxLayerCount < count) {
                maxLayerCount = count;
            }
        }

        return targetLayerCount + cake.getLayerType().getValue() >= maxLayerCount;
    }

    public void addToJLayeredPane(final JLayeredPane layeredPane) {
        layeredPane.add(mLabelTarget);
        for (var label : mLabelTotalLayers) {
            layeredPane.add(label);
        }
        layeredPane.add(mLabelSpot);
    }
}
