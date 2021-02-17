package assignment.game.object;

import assignment.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class World {
    private ArrayList<City> mCities = new ArrayList<City>();

    public World() {
        for (int i = 0; i < Config.MAX_CITY_SIZE; ++i) {
            City city = new City();
            mCities.add(city);
        }
    }

    public ArrayList<City> getCities() {
        return mCities;
    }

    public ArrayList<Player> getHighestCakeOwners() {
        int maxHeight = 0;
        ArrayList<Player> owners = new ArrayList<Player>();

        for (City city : mCities) {
            for (Spot spot : city.getSpots()) {
                Player owner = spot.getOwnerOrNull();
                if (owner == null) {
                    continue;
                }

                int height = spot.getCakeHeight();
                if (maxHeight < height) {
                    maxHeight = height;
                    owners.clear();
                    owners.add(owner);
                } else if (maxHeight == height) {
                    owners.add(owner);
                }
            }
        }

        return owners;
    }

    public ArrayList<Player> getMostCakeOwnersPerCity() {
        ArrayList<Player> owners = new ArrayList<Player>();

        for (City city : mCities) {

            int maxCount = 0;
            HashMap<Player, Integer> ownersPerCity = new HashMap<Player, Integer>();

            for (Spot spot : city.getSpots()) {
                Player owner = spot.getOwnerOrNull();
                if (owner == null) {
                    continue;
                }

                if (!ownersPerCity.containsKey(owner)) {
                    ownersPerCity.put(owner, 0);
                }

                int count = ownersPerCity.get(owner) + 1;
                ownersPerCity.replace(owner, count);

                if (maxCount < count) {
                    maxCount = count;
                }
            }

            Iterator it = ownersPerCity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if ((int) pair.getValue() != maxCount) {
                    it.remove();
                }
            }

            if (ownersPerCity.size() == 1) {
                owners.add(ownersPerCity.entrySet().iterator().next().getKey());
            }
        }

        return owners;
    }

    public void clearTargetImages() {
        for (City city : mCities) {
            for (Spot spot : city.getSpots()) {
                spot.setTargetVisible(false);
            }
        }
    }

    public void drawTargetImages(final CardType cardType, final PlayerPositionType playerPositionType) {
        for (City city : mCities) {
            int index = City.INDICES_2D_MAP[playerPositionType.getIndex()][cardType.getIndex()];
            city.getSpots().get(index).setTargetVisible(true);
        }
    }

    public void clearAllSpots() {
        for (City city : getCities()) {
            for (Spot spot : city.getSpots()) {
                spot.clearSpot();
            }
        }
    }
}
