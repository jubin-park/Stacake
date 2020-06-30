package assignment.panel;

import assignment.frame.FrameMain;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public final class PanelManager {
    private static PanelManager sInstance;

    private CardLayout mCardLayout = new CardLayout();
    private HashMap<PanelType, JPanel> mPanels = new HashMap<PanelType, JPanel>();
    private PanelType mCurrentPanelType;

    private PanelManager() {
        FrameMain.getInstance().setLayout(mCardLayout);
    }

    public static PanelManager getInstance() {
        if (sInstance == null) {
            sInstance = new PanelManager();
        }
        return sInstance;
    }

    public PanelType getCurrentPanelType() {
        return mCurrentPanelType;
    }

    public JPanel getCurrentPanel() {
        return mPanels.get(mCurrentPanelType);
    }

    public void showPanel(final PanelType panelType) {
        assert (panelType != null) : "panelType cannot be null";
        assert (mPanels.containsKey(panelType)) : "panelType does not exist";
        mCurrentPanelType = panelType;
        mCardLayout.show(FrameMain.getInstance().getContentPane(), panelType.toString());
        ((IUpdatable) getCurrentPanel()).updateComponents();
    }

    public void addPanel(PanelType panelType, JPanel panel) {
        mPanels.put(panelType, panel);
        FrameMain.getInstance().add(panelType.toString(), panel);
    }
}
