package assignment.game.object;

public final class Building {
    private final MarkerColor mColor;
    private final BuildingStory mStory;

    public Building(final MarkerColor color, final BuildingStory story) {
        mColor = color;
        mStory = story;
    }

    public MarkerColor getType() {
        return mColor;
    }

    public BuildingStory getStory() {
        return mStory;
    }
}
