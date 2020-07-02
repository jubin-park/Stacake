package assignment.game.object;

import assignment.Config;
import java.util.ArrayList;
import javax.swing.*;

public final class City {
    private static int[][] INDICES_2D_MAP= new int[][] {
            {0, 1, 2, 3, 4, 5, 6, 7, 8},
            {2, 5, 8, 1, 4, 7, 0, 3, 6},
            {8, 7, 6, 5, 4, 3, 2, 1, 0},
            {6, 3, 0, 7, 4, 1, 8, 5, 2}
    };

    /*
    {6, 3, 0, 7, 4, 1, 8, 5, 2},
    {8, 7, 6, 5, 4, 3, 2, 1, 0},
    {2, 5, 8, 1, 4, 7, 0, 3, 6}
     */

    private ArrayList<Spot> mSpots = new ArrayList<Spot>();

    private JLayeredPane mLayeredPane;

    public City() {
        mLayeredPane = new JLayeredPane();
        mLayeredPane.setOpaque(false);

        for (int i = 0; i < Config.SPOT_COUNT_PER_CITY; ++i) {
            var spot = new Spot();
            mSpots.add(spot);

            int x = 8 + (i % Config.SPOT_COLUMN_PER_CITY) * Config.SPOT_IMAGE_WIDTH;
            int y = 8 + (i / Config.SPOT_COLUMN_PER_CITY) * Config.SPOT_IMAGE_HEIGHT;

            spot.getLabelSpot().setBounds(x, y, Config.SPOT_IMAGE_WIDTH, Config.SPOT_IMAGE_HEIGHT);

            var labels = spot.getLabelTotalLayers();
            labels[0].setHorizontalAlignment(SwingConstants.CENTER);
            labels[0].setVerticalAlignment(SwingConstants.BOTTOM);
            labels[0].setBounds(x + Config.SPOT_LABEL_OFFSET_X, y + Config.SPOT_LABEL_OFFSET_Y, Config.SPOT_LABEL_WIDTH, Config.SPOT_LABEL_HEIGHT);

            labels[1].setHorizontalAlignment(SwingConstants.LEFT);
            labels[1].setVerticalAlignment(SwingConstants.CENTER);
            labels[1].setBounds(x + Config.SPOT_LABEL_OFFSET_X, y + Config.SPOT_LABEL_OFFSET_Y, Config.SPOT_LABEL_WIDTH, Config.SPOT_LABEL_HEIGHT);

            labels[2].setHorizontalAlignment(SwingConstants.CENTER);
            labels[2].setVerticalAlignment(SwingConstants.TOP);
            labels[2].setBounds(x + Config.SPOT_LABEL_OFFSET_X, y + Config.SPOT_LABEL_OFFSET_Y, Config.SPOT_LABEL_WIDTH, Config.SPOT_LABEL_HEIGHT);

            labels[3].setHorizontalAlignment(SwingConstants.RIGHT);
            labels[3].setVerticalAlignment(SwingConstants.CENTER);
            labels[3].setBounds(x + Config.SPOT_LABEL_OFFSET_X, y + Config.SPOT_LABEL_OFFSET_Y, Config.SPOT_LABEL_WIDTH, Config.SPOT_LABEL_HEIGHT);
            spot.updateLabels();

            spot.getLabelTarget().setBounds(x, y, Config.TARGET_IMAGE_WIDTH, Config.TARGET_IMAGE_HEIGHT);

            spot.addToJLayeredPane(mLayeredPane);
        }
    }

    public void updatePreview(final CardType card, final PlayerPositionType playerPositionType) {
        for (var spot : mSpots) {
            spot.setTargetVisible(false);
        }

        int index = INDICES_2D_MAP[playerPositionType.getIndex()][card.getIndex()];
        mSpots.get(index).setTargetVisible(true);
    }

    public ArrayList<Spot> getSpots() {
        return mSpots;
    }

    public Spot getSpotByCake(final CardType card, final PlayerPositionType playerPositionType) {
        return mSpots.get(INDICES_2D_MAP[playerPositionType.getIndex()][card.getIndex()]);
    }

    public JLayeredPane getLayeredPane() {
        return mLayeredPane;
    }
}
