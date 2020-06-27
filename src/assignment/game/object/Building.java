package assignment.game.object;

public final class Building {
    private final MarkerColor mType;
    private final BuildingStoryType mStory;

    public Building(final MarkerColor type, final BuildingStoryType story) {
        mType = type;
        mStory = story;
    }

    public MarkerColor getType() {
        return mType;
    }

    public BuildingStoryType getStory() {
        return mStory;
    }
}
