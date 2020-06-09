package assignment.panel;

import assignment.window.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
// https://stackoverflow.com/questions/42964669/placing-button-panel-in-center-java-swing

public class IntroPanel extends JPanel {
    private final MainWindow mMainWindow;
    private GridBagConstraints mGridBagConstraints = new GridBagConstraints();

    public IntroPanel(MainWindow mainWindow)
    {
        mMainWindow = mainWindow;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        // title
        addOnPanel(this, new JLabel("<html><h1><strong><i>Manhattan Game</i></strong></h1><hr></html>"), 1, 0, 1, 1);

        // buttons
        var buttonSinglePlay = new JButton("Single Play");
        buttonSinglePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        var buttonNetPlay = new JButton("Net Play");
        buttonNetPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        var buttonSetting = new JButton("Settings");
        buttonSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        var buttonExit = new JButton("Shutdown");
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container frame = ((JButton) e.getSource()).getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        JPanel panelButtons = new JPanel(new GridBagLayout());
        mGridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        addOnPanel(panelButtons, buttonSinglePlay, 0, 0, 1, 1);
        addOnPanel(panelButtons, buttonNetPlay, 0, 1, 1, 1);
        addOnPanel(panelButtons, buttonSetting, 0, 2, 1, 1);
        addOnPanel(panelButtons, buttonExit, 0, 3, 1, 1);
        addOnPanel(this, panelButtons, 1, 1, 1, 1);

        setVisible(true);
    }

    private void addOnPanel(JPanel panel, JComponent component, int gridX, int gridY, int gridWidth, int gridHeight) {
        mGridBagConstraints.gridx = gridX;
        mGridBagConstraints.gridy = gridY;
        mGridBagConstraints.gridwidth = gridWidth;
        mGridBagConstraints.gridheight = gridHeight;
        panel.add(component, mGridBagConstraints);
    }
}
