package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
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

    public static void setUserId(String sUserId) {
        Config.sUserId = sUserId;
    }

    public static byte getLimitSecondsPerTurn() {
        return sLimitSecondsPerTurn;
    }

    public static void setLimitSecondsPerTurn(byte sLimitSecondsPerTurn) {
        Config.sLimitSecondsPerTurn = sLimitSecondsPerTurn;
    }

    public static byte getSoundEffectVolume() {
        return sSoundEffectVolume;
    }

    public static void setSoundEffectVolume(byte sSoundEffectVolume) {
        Config.sSoundEffectVolume = sSoundEffectVolume;
    }

    public static boolean getDebugMode() {
        return sbDebugMode;
    }

    public static void setDebugMode(boolean isDebugMode) {
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
