package assignment.panel;

import assignment.MainWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
// https://stackoverflow.com/questions/42964669/placing-button-panel-in-center-java-swing

public class PanelIntro extends JPanel {

    private JLabel mLabel;
    private JButton mButton;
    private MainWindow mMainWindow;
    
    public PanelIntro(MainWindow mainWindow)
    {
        mMainWindow = mainWindow;
        setBackground(Color.LIGHT_GRAY);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("<html><h1><strong><i>Manhattan Game</i></strong></h1><hr></html>"), gbc);

        gbc.gridwidth = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelButtons = new JPanel(new GridBagLayout());
        panelButtons.add(new JButton("Single Play"), gbc);
        panelButtons.add(new JButton("Net Play"), gbc);

        gbc.weighty = 1;
        add(panelButtons, gbc);
        setVisible(true); 
    }
}
