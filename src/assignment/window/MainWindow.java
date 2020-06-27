package assignment.window;

import assignment.panel.IntroPanel;
import assignment.panel.PanelManager;
import assignment.panel.PanelType;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    private static MainWindow sInstance;

    public MainWindow(int width, int height) {
        super("Manhattan Game");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            MainWindow.this.setVisible(false);
            MainWindow.this.dispose();
            }
        });

        pack();
        setSize(width, height);
        setLocationRelativeTo(null); // center

        if (sInstance == null) {
            sInstance = this;
        }

        var panelManager = PanelManager.getInstance();
        panelManager.addPanel(PanelType.INTRO, new IntroPanel());
        panelManager.showPanel(PanelType.INTRO);
    }

    public static MainWindow getInstance() {
        return sInstance;
    }
}