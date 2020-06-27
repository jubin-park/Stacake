package assignment.game.object;

public class Player {
    protected String mId;
    protected Marker mMarker;

    public Player(String id) {
        mId = id;
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
