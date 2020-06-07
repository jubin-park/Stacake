package assignment;

import assignment.panel.PanelIntro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// https://msource.tistory.com/5

public class MainWindow extends JFrame {

    private CardLayout mCardLayout = new CardLayout();

    public MainWindow() {
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
        add("one", new PanelIntro(this));

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public void changeNextPanel() {
        mCardLayout.next(getContentPane());
    }
}