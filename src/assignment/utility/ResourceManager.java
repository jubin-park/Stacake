package assignment.utility;

import assignment.Program;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class ResourceManager {
    public static final String CAKES_IMAGE_PATH = "resources/images/cakes.png";
    public static final String CARDS_IMAGE_PATH = "resources/images/cards.png";
    public static final String SPOTS_IMAGE_PATH = "resources/images/spots.png";
    public static final String MARKERS_IMAGE_PATH = "resources/images/markers.png";

    private static ResourceManager mInstance;

    private BufferedImage mImageSetCake;
    private BufferedImage mImageSetCard;
    private BufferedImage mImageSetSpot;
    private BufferedImage mImageSetMarker;

    private ResourceManager() {
        try {
            mImageSetCake = ImageIO.read(Program.class.getResourceAsStream(CAKES_IMAGE_PATH));
            mImageSetCard = ImageIO.read(Program.class.getResourceAsStream(CARDS_IMAGE_PATH));
            mImageSetSpot = ImageIO.read(Program.class.getResourceAsStream(SPOTS_IMAGE_PATH));
            mImageSetMarker = ImageIO.read(Program.class.getResourceAsStream(MARKERS_IMAGE_PATH));
        } catch (IOException ex) {
            assert (false) : ex;
        }
    }

    public static ResourceManager getInstance() {
        if (mInstance == null) {
            mInstance = new ResourceManager();
        }

        return mInstance;
    }

    public BufferedImage getImageSetCake() {
        return mImageSetCake;
    }

    public BufferedImage getImageSetCard() {
        return mImageSetCard;
    }

    public BufferedImage getImageSetSpot() {
        return mImageSetSpot;
    }

    public BufferedImage getImageSetMarker() {
        return mImageSetMarker;
    }
}
