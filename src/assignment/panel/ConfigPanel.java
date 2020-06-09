package assignment.panel;

import assignment.window.MainWindow;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    private final MainWindow mMainWindow;
    private GridBagConstraints mGridBagConstraints = new GridBagConstraints();

    public ConfigPanel(MainWindow mainWindow) {
        mMainWindow = mainWindow;
    }
}
