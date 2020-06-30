package assignment;

import assignment.utility.ResourceManager;
import assignment.window.MainWindow;

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
                final MainWindow mainWindow = new MainWindow(900, 732);
                mainWindow.setVisible(true);
            }
        });
    }
}
