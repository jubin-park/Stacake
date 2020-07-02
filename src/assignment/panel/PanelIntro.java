package assignment.panel;

import assignment.utility.ResourceManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PanelIntro extends JPanel implements IUpdatable {
    public PanelIntro()
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        JPanel panelButtonList = new JPanel(new GridLayout(6, 1));

        // title
        var labelTitle = new JLabel("<html><h1><strong><i>- Stacake -</i></strong></h1><p>Stack pieces of cake<hr></html>");
        labelTitle.setOpaque(true);
        labelTitle.setBackground(Color.WHITE);
        panelButtonList.add(labelTitle);

        // buttons
        var buttonSinglePlay = new JButton("Single Play");
        buttonSinglePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                var panelManager = PanelManager.getInstance();
                var panelInGame = new PanelInGame(new String[] {});
                panelManager.addPanel(PanelType.INGAME, panelInGame);
                panelManager.showPanel(PanelType.INGAME);
                panelInGame.start();
            }
        });
        panelButtonList.add(buttonSinglePlay);

        var buttonCreateNetPlay = new JButton("Create NetPlay");
        buttonCreateNetPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        });
        panelButtonList.add(buttonCreateNetPlay);

        var buttonJoinNetPlay = new JButton("Join NetPlay");
        buttonJoinNetPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        });
        panelButtonList.add(buttonJoinNetPlay);

        var buttonConfig = new JButton("Configuration");
        buttonConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                var panelManager = PanelManager.getInstance();
                panelManager.addPanel(PanelType.CONFIG, new PanelConfig());
                panelManager.showPanel(PanelType.CONFIG);
            }
        });
        panelButtonList.add(buttonConfig);

        var buttonExit = new JButton("Shutdown");
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Container frame = ((JButton) e.getSource()).getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });
        panelButtonList.add(buttonExit);

        add(panelButtonList);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ResourceManager.getInstance().getImageBackground(), 0, 0, null);
    }

    @Override
    public void updateComponents() {

    }
}
