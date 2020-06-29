package assignment.game.object;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    protected String mId;
    protected Marker mMarker;
    protected HashMap<BuildingStory, Integer> mRemainBuildingCounts = new HashMap<BuildingStory, Integer>();
    protected ArrayList<BuildingStory> mUsableBuildings = new ArrayList<BuildingStory>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();

    public Player(String id) {
        mId = id;
        mRemainBuildingCounts.put(BuildingStory.ONE, 2);
        mRemainBuildingCounts.put(BuildingStory.TWO, 4);
        mRemainBuildingCounts.put(BuildingStory.THREE, 6);
        mRemainBuildingCounts.put(BuildingStory.FOUR, 12);
    }

    public int getBuildingCount(final BuildingStory buildingStory) {
        return mRemainBuildingCounts.get(buildingStory);
    }

    public void takeBuilding(final BuildingStory buildingStory) {
        assert (mRemainBuildingCounts.get(buildingStory).intValue() > 0);

        mUsableBuildings.add(buildingStory);
        int count = mRemainBuildingCounts.get(buildingStory);
        mRemainBuildingCounts.put(buildingStory, count - 1);
    }

    public String getId() {
        return mId;
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker marker) {
        mMarker = marker;
    }
}
