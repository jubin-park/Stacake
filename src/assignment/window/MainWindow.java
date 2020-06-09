package assignment.window;

import assignment.panel.ConfigPanel;
import assignment.panel.IntroPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// https://msource.tistory.com/5

public class MainWindow extends JFrame {
    private CardLayout mCardLayout = new CardLayout();

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
        add("intro", new IntroPanel(this));
        add("config", new ConfigPanel(this));

        pack();
        setSize(width, height);
        setLocationRelativeTo(null); // center
    }

    public void changePreviousPanel() {
        mCardLayout.previous(getContentPane());
    }

    public void changeNextPanel() {
        mCardLayout.next(getContentPane());
    }

    public void showPanel(String name) {
        mCardLayout.show(getContentPane(), name);
    }
}