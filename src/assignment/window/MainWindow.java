package assignment.window;

import assignment.panel.ConfigPanel;
import assignment.panel.IUpdatable;
import assignment.panel.IntroPanel;
import assignment.panel.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

// https://msource.tistory.com/5

public class MainWindow extends JFrame {
    private CardLayout mCardLayout = new CardLayout();
    private HashMap<PanelType, JPanel> mPanels = new HashMap<PanelType, JPanel>();
    private PanelType mCurrentPanelType;

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

        setLayout(mCardLayout);

        addPanel(PanelType.INTRO, new IntroPanel(this));
        addPanel(PanelType.CONFIG, new ConfigPanel(this));
        showPanel(PanelType.INTRO);

        pack();
        setSize(width, height);
        setLocationRelativeTo(null); // center
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
        mCardLayout.show(getContentPane(), panelType.toString());
        ((IUpdatable) getCurrentPanel()).updateComponents();
    }

    private void addPanel(PanelType panelType, JPanel panel) {
        mPanels.put(panelType, panel);
        add(panelType.toString(), panel);
    }
}