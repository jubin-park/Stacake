package assignment.utility;

import assignment.Config;
import assignment.Program;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class ResourceManager {
    private static final String IMAGE_DIRECTORY = "resources/images/";

    public static final String BACKGROUND_IMAGE_PATH = "background.png";
    public static final String BACKGROUND2_IMAGE_PATH = "background-2.png";
    public static final String CAKES_IMAGE_PATH = "cakes.png";
    public static final String CARDS_IMAGE_PATH = "cards.png";
    public static final String SPOTS_IMAGE_PATH = "spots.png";
    public static final String MARKERS_IMAGE_PATH = "markers.png";
    public static final String TARGET_IMAGE_PATH = "target.png";
    public static final String SINGLE_GAME_IMAGE_PATH = "single-game.png";
    public static final String ONLINE_GAME_IMAGE_PATH = "online-game.png";
    public static final String CONFIG_IMAGE_PATH = "config.png";
    public static final String SHUTDOWN_IMAGE_PATH = "shutdown.png";

    private static ResourceManager mInstance;

    private BufferedImage mImageBackground;
    private BufferedImage mImageBackground2;
    private BufferedImage mImageSetCake;
    private BufferedImage mImageSetCard;
    private BufferedImage mImageSetSpot;
    private BufferedImage mImageSetMarker;
    private BufferedImage mImageTarget;
    private ImageIcon mImageIconDefaultSpot;
    private BufferedImage mImageSingleGame;
    private BufferedImage mImageOnlineGame;
    private BufferedImage mImageConfig;
    private BufferedImage mImageShutdown;

    private ResourceManager() {
        try {
            mImageBackground = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + BACKGROUND_IMAGE_PATH));
            mImageBackground2 = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + BACKGROUND2_IMAGE_PATH));
            mImageSetCake = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + CAKES_IMAGE_PATH));
            mImageSetCard = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + CARDS_IMAGE_PATH));
            mImageSetSpot = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + SPOTS_IMAGE_PATH));
            mImageSetMarker = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + MARKERS_IMAGE_PATH));
            mImageTarget = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + TARGET_IMAGE_PATH));
            mImageIconDefaultSpot = new ImageIcon(mImageSetSpot.getSubimage(0, 0, Config.SPOT_IMAGE_WIDTH, Config.SPOT_IMAGE_HEIGHT));
            mImageSingleGame = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + SINGLE_GAME_IMAGE_PATH));
            mImageOnlineGame = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + ONLINE_GAME_IMAGE_PATH));
            mImageConfig = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + CONFIG_IMAGE_PATH));
            mImageShutdown = ImageIO.read(Program.class.getResourceAsStream(IMAGE_DIRECTORY + SHUTDOWN_IMAGE_PATH));

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

    public BufferedImage getImageBackground() {
        return mImageBackground;
    }

    public BufferedImage getImageBackground2() {
        return mImageBackground2;
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

    public BufferedImage getImageTarget() {
        return mImageTarget;
    }

    public ImageIcon getImageIconDefaultSpot() {
        return mImageIconDefaultSpot;
    }

    public BufferedImage getImageSingleGame() {
        return mImageSingleGame;
    }

    public BufferedImage getImageOnlineGame() {
        return mImageOnlineGame;
    }

    public BufferedImage getImageConfig() {
        return mImageConfig;
    }

    public BufferedImage getImageShutdown() {
        return mImageShutdown;
    }
}
