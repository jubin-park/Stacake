package assignment.frame;

import assignment.panel.PanelIntro;
import assignment.panel.PanelManager;
import assignment.panel.PanelType;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameMain extends JFrame {
    private static FrameMain sInstance;

    public FrameMain(int width, int height) {
        super("Manhattan Game");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            FrameMain.this.setVisible(false);
            FrameMain.this.dispose();
            }
        });

        pack();
        setSize(width, height);
        setLocationRelativeTo(null); // center

        if (sInstance == null) {
            sInstance = this;
        }

        var panelManager = PanelManager.getInstance();
        panelManager.addPanel(PanelType.INTRO, new PanelIntro());
        panelManager.showPanel(PanelType.INTRO);
    }

    public static FrameMain getInstance() {
        return sInstance;
    }
}