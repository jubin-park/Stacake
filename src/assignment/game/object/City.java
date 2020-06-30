package assignment.game.object;

import assignment.Config;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public final class City {
    private ArrayList<Spot> mSpots = new ArrayList<Spot>();
    private JLayeredPane mLayeredPane;

    public City() {
        mLayeredPane = new JLayeredPane();
        mLayeredPane.setOpaque(false);
        //mLayeredPane.setPreferredSize(new Dimension(486, 324));

        for (int i = 0; i < Config.SPOT_COUNT_PER_CITY; ++i) {
            var spot = new Spot();
            mSpots.add(spot);

            int x = (i % Config.SPOT_COLUMN_PER_CITY) * 49;
            int y = (i / Config.SPOT_COLUMN_PER_CITY) * 49;

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

            spot.updateLabel();
            spot.addTo(mLayeredPane);
        }
    }

    public Spot getSpot(final int index) {
        return mSpots.get(index);
    }

    public ArrayList<Spot> getSpots() {
        return mSpots;
    }

    public JLayeredPane getLayeredPane() {
        return mLayeredPane;
    }
}
