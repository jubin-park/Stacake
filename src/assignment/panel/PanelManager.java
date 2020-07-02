package assignment.panel;

import assignment.frame.FrameMain;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public final class PanelManager {
    private static PanelManager sInstance;

    private CardLayout mCardLayout = new CardLayout();
    private ArrayList<JPanel> mPanels = new ArrayList<JPanel>();

    private PanelManager() {
        FrameMain.getInstance().setLayout(mCardLayout);
    }

    public static PanelManager getInstance() {
        if (sInstance == null) {
            sInstance = new PanelManager();
        }

        return sInstance;
    }

    public JPanel getCurrentPanel() {
        assert (mPanels.size() > 0);

        return mPanels.get(mPanels.size() - 1);
    }

    public void gotoPanel(final JPanel panel) {
        var frameMain = FrameMain.getInstance();
        frameMain.add(panel.toString(), panel);
        mCardLayout.show(frameMain.getContentPane(), panel.toString());
        mPanels.add(panel);
    }

    public void popPanel() {
        mPanels.remove(mPanels.size() - 1);
    }

    public CardLayout getCardLayout() {
        return mCardLayout;
    }
}
