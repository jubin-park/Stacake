package assignment.frame;

import assignment.Config;
import assignment.panel.PanelIntro;
import assignment.panel.PanelManager;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class FrameMain extends JFrame {
    private static FrameMain sInstance;
    private boolean mbRunning;

    public FrameMain(final int width, final int height) {
        super(Config.FRAME_TITLE);

        if (sInstance == null) {
            sInstance = this;
        }

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            FrameMain.this.setVisible(false);
            FrameMain.this.dispose();
            mbRunning = false;
            }
        });

        pack();
        setSize(width, height);
        setLocationRelativeTo(null); // center

        PanelManager.getInstance().gotoPanel(new PanelIntro());
    }

    public static FrameMain getInstance() {
        assert (sInstance != null);

        return sInstance;
    }

    public boolean isRunning() {
        return mbRunning;
    }

    public void setRunning(final boolean bRunning) {
        mbRunning = bRunning;
    }
}