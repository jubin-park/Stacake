package assignment.game.object;

public final class Building {
    private final PlayerColorType mType;
    private final StoryType mStory;

    public Building(final PlayerColorType type, final StoryType story) {
        mType = type;
        mStory = story;
    }

    public PlayerColorType getType() {
        return mType;
    }

    public StoryType getStory() {
        return mStory;
    }
}
