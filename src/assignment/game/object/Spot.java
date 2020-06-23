package assignment.game.object;

import java.util.ArrayList;

public final class Spot {
    private ArrayList<Building> mBuildings = new ArrayList<>();

    public void stackBuilding(final Building building) {
        mBuildings.add(building);
    }

    public int getBuildingStoryCount(final PlayerColorType type) {
        int count = 0;

        for (var building : mBuildings) {
            if (building.getType() == type) {
                count += building.getStory().getValue();
            }
        }

        return count;
    }
}
