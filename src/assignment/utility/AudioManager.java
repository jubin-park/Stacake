package assignment.utility;

import assignment.Config;
import assignment.Program;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public final class AudioManager {
    private AudioManager() {
    }

    public static void play(final String filename, byte volume) {
        // volume : [0, 100]
        try {
            String path = Program.class.getResource(String.format("./resources/sounds/%s", filename)).getPath();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float dB = (float) (Math.log(volume / 100.f) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception ex) {

        }
    }

    public static void play(final String filename) {
        play(filename, Config.getSoundEffectVolume());
    }
}
