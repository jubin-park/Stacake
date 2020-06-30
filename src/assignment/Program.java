package assignment;

import assignment.utility.ResourceManager;
import assignment.frame.FrameMain;

import javax.swing.*;

public class Program {
    public static void main(String[] args) {
        if (args.length > 0) {
            Config.setUserId(args[0]);
        }
        else {
            Config.setDebugMode(false);
        }
        Config.loadAndUpdateProperties();

        ResourceManager.getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final FrameMain frameMain = new FrameMain(900, 732);
                frameMain.setVisible(true);
            }
        });
    }
}
