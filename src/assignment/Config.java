package assignment;

public class Config {
    private static String sUserId = "Me";
    private static byte sLimitSecondsPerTurn = 30;
    private static byte sSoundEffectVolume = 100;
    private static boolean bDebugMode = true;

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
        return bDebugMode;
    }

    public static void setDebugMode(boolean isDebugMode) {
        if (Config.bDebugMode) {
            Config.bDebugMode = isDebugMode;
        }
    }
}
