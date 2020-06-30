package assignment.game.object;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    protected String mId;
    protected Marker mMarker;
    protected ArrayList<BuildingLayerType> mRemainBuildings = new ArrayList<BuildingLayerType>();
    protected ArrayList<BuildingLayerType> mUsableBuildings = new ArrayList<BuildingLayerType>();
    protected ArrayList<CardType> mCards = new ArrayList<CardType>();

    public Player(String id) {
        mId = id;
        for (int i = 0; i < 2; ++i) {
            mRemainBuildings.add(BuildingLayerType.ONE);
        }
        for (int i = 0; i < 4; ++i) {
            mRemainBuildings.add(BuildingLayerType.TWO);
        }
        for (int i = 0; i < 6; ++i) {
            mRemainBuildings.add(BuildingLayerType.THREE);
        }
        for (int i = 0; i < 12; ++i) {
            mRemainBuildings.add(BuildingLayerType.FOUR);
        }
    }

    public int getBuildingCount(final BuildingLayerType buildingLayerType) {
        int count = 0;
        for (var story : mRemainBuildings) {
            if (story == buildingLayerType) {
                ++count;
            }
        }
        return count;
    }

    public void takeBuilding(final BuildingLayerType buildingLayerType) {
        if (mRemainBuildings.remove(buildingLayerType)) {
            mUsableBuildings.add(buildingLayerType);
        } else {
            assert (false);
        }
    }

    public void takeCardFromDummy(ArrayList<CardType> dummyCards) {
        assert (!dummyCards.isEmpty());

        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(dummyCards.size());

        mCards.add(dummyCards.get(index));
        dummyCards.remove(index);
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

    public ArrayList<CardType> getCards() {
        return mCards;
    }
}
