package assignment.panel;

import assignment.window.MainWindow;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public final class PanelManager {
    private static PanelManager sInstance;

    private CardLayout mCardLayout = new CardLayout();
    private HashMap<PanelType, JPanel> mPanels = new HashMap<PanelType, JPanel>();
    private PanelType mCurrentPanelType;

    private PanelManager() {
        MainWindow.getInstance().setLayout(mCardLayout);
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

    public void showPanel(PanelType panelType) {
        assert (panelType != null) : "panelType cannot be null";
        assert (mPanels.containsKey(panelType)) : "panelType does not exist";
        mCurrentPanelType = panelType;
        mCardLayout.show(MainWindow.getInstance().getContentPane(), panelType.toString());
        ((IUpdatable) getCurrentPanel()).updateComponents();
    }

    public void addPanel(PanelType panelType, JPanel panel) {
        mPanels.put(panelType, panel);
        MainWindow.getInstance().add(panelType.toString(), panel);
    }
}
