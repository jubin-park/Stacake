package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class Config {
    /*
     *  Game Configuration
     */
    public static final int FRAME_WIDTH = 912;
    public static final int FRAME_HEIGHT = 740;
    public static final String FRAME_TITLE = "Stacake";

    public static final int ROUND_CARD_COUNT = 4;

    public static final int HUD_HEIGHT = 214;

    public static final int CAKE_IMAGE_WIDTH = 32;
    public static final int CAKE_IMAGE_HEIGHT = 96;

    public static final int CARD_IMAGE_WIDTH = 64;
    public static final int CARD_IMAGE_HEIGHT = 64;

    public static final int SPOT_IMAGE_WIDTH = 49;
    public static final int SPOT_IMAGE_HEIGHT = 49;
    public static final int SPOT_LABEL_WIDTH = 37;
    public static final int SPOT_LABEL_HEIGHT = 41;
    public static final int SPOT_LABEL_OFFSET_X = 6;
    public static final int SPOT_LABEL_OFFSET_Y = 4;

    public static final int MARKER_IMAGE_WIDTH = 49;
    public static final int MARKER_IMAGE_HEIGHT = 49;
    public static final int MARKER_PANE_WIDTH = 64;
    public static final int MARKER_PANE_HEIGHT = 72;

    public static final int TARGET_IMAGE_WIDTH = 49;
    public static final int TARGET_IMAGE_HEIGHT = 49;

    public static final int MAX_ROUND_COUNT = 4;
    public static final int MAX_PLAYER_SIZE = 4;
    public static final int MAX_CITY_SIZE = 6;
    public static final int MAX_SELECTING_CAKE_COUNT = 6;

    public static final int SPOT_COUNT_PER_CITY = 9;
    public static final int SPOT_ROW_PER_CITY = 2;
    public static final int SPOT_COLUMN_PER_CITY = 3;

    /*
     * Property Configuration
     */
    private static final String CONFIG_FILE_NAME = "config.properties";

    private static final String PROPERTY_USER_ID = "user_id";
    private static final String PROPERTY_LIMIT_SECONDS_PER_TURN = "limit_seconds_per_turn";
    private static final String PROPERTY_SOUND_EFFECT_VOLUME = "sound_effect_volume";

    private static final String DEFAULT_USER_ID = "Java-man";
    private static final String DEFAULT_LIMIT_SECONDS_PER_TURN = "20";
    private static final String DEFAULT_SOUND_EFFECT_VOLUME = "95";

    private static String sUserId = DEFAULT_USER_ID;
    private static byte sLimitSecondsPerTurn = Byte.parseByte(DEFAULT_LIMIT_SECONDS_PER_TURN);
    private static byte sSoundEffectVolume = Byte.parseByte(DEFAULT_SOUND_EFFECT_VOLUME);
    private static boolean sbDebugMode = true;

    private Config() {

    }

    public static String getUserId() {
        return sUserId;
    }

    public static void setUserId(final String sUserId) {
        Config.sUserId = sUserId;
    }

    public static byte getLimitSecondsPerTurn() {
        return sLimitSecondsPerTurn;
    }

    public static void setLimitSecondsPerTurn(final byte sLimitSecondsPerTurn) {
        Config.sLimitSecondsPerTurn = sLimitSecondsPerTurn;
    }

    public static byte getSoundEffectVolume() {
        return sSoundEffectVolume;
    }

    public static void setSoundEffectVolume(final byte sSoundEffectVolume) {
        Config.sSoundEffectVolume = sSoundEffectVolume;
    }

    public static boolean getDebugMode() {
        return sbDebugMode;
    }

    public static void setDebugMode(final boolean isDebugMode) {
        if (Config.sbDebugMode) {
            Config.sbDebugMode = isDebugMode;
        }
    }

    public static void saveProperties() {
        File configFile = new File(CONFIG_FILE_NAME);
        try {
            Properties props = new Properties();

            props.setProperty(PROPERTY_USER_ID, sUserId);
            props.setProperty(PROPERTY_LIMIT_SECONDS_PER_TURN, Byte.toString(sLimitSecondsPerTurn));
            props.setProperty(PROPERTY_SOUND_EFFECT_VOLUME, Byte.toString(sSoundEffectVolume));

            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "game settings");
            writer.close();

        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
    }

    public static void loadAndUpdateProperties() {
        File configFile = new File(CONFIG_FILE_NAME);
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            sUserId = props.getProperty(PROPERTY_USER_ID, DEFAULT_USER_ID);
            sLimitSecondsPerTurn = Byte.parseByte(props.getProperty(PROPERTY_LIMIT_SECONDS_PER_TURN, DEFAULT_LIMIT_SECONDS_PER_TURN));
            sSoundEffectVolume = Byte.parseByte(props.getProperty(PROPERTY_SOUND_EFFECT_VOLUME, DEFAULT_SOUND_EFFECT_VOLUME));

            reader.close();

        } catch (FileNotFoundException ex) {
            // file does not exist
            saveProperties();
        } catch (IOException ex) {
            // I/O error
        }
    }
}
