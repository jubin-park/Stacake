package assignment.game.object;

import assignment.Config;
import java.util.ArrayList;

public final class City {
    private ArrayList<Spot> mSpots = new ArrayList<Spot>();

    public City() {
        for (int i = 0; i < Config.SPOT_COUNT_PER_CITY; ++i) {
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
