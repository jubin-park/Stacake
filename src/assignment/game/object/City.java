package assignment.game.object;

import java.util.ArrayList;

public final class City {
    private static final int SPOT_COUNT = 9;

    private ArrayList<Spot> mSpots = new ArrayList<Spot>();

    public City() {
        for (int i = 0; i < SPOT_COUNT; ++i) {
            mSpots.add(new Spot());
        }
    }

    public Spot getSpot(final int index) {
        return mSpots.get(index);
    }

    public ArrayList<Spot> getSpots() {
        return mSpots;
    }
}
