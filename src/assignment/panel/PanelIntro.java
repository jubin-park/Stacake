package assignment.panel;

import assignment.utility.AudioManager;
import assignment.utility.ResourceManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PanelIntro extends JPanel {
    public PanelIntro()
    {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        JPanel panelButtonList = new JPanel(new GridLayout(4, 1));

        // title
        var labelTitle = new JLabel("<html><h1><strong><i>- Stacake -</i></strong></h1><p>Stack pieces of cake<hr></html>");
        labelTitle.setOpaque(true);
        labelTitle.setBackground(Color.WHITE);
        panelButtonList.add(labelTitle);

        // buttons
        var buttonSinglePlay = new JButton("혼자 놀기", new ImageIcon(ResourceManager.getInstance().getImageSingleGame()));
        buttonSinglePlay.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonSinglePlay.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonSinglePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                var panelManager = PanelManager.getInstance();
                var panelInGame = new PanelInGame(new String[] {});
                panelManager.gotoPanel(panelInGame);
                panelInGame.start();
            }
        });
        panelButtonList.add(buttonSinglePlay);

        var buttonCreateNetPlay = new JButton("같이 놀기", new ImageIcon(ResourceManager.getInstance().getImageOnlineGame()));
        buttonCreateNetPlay.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonCreateNetPlay.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonCreateNetPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        });
        //panelButtonList.add(buttonCreateNetPlay);

        var buttonJoinNetPlay = new JButton("방 만들기", new ImageIcon(ResourceManager.getInstance().getImageOnlineGame()));
        buttonJoinNetPlay.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonJoinNetPlay.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonJoinNetPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        });
        //panelButtonList.add(buttonJoinNetPlay);

        var buttonConfig = new JButton("환경 설정", new ImageIcon(ResourceManager.getInstance().getImageConfig()));
        buttonConfig.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonConfig.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                var panelManager = PanelManager.getInstance();
                panelManager.gotoPanel(new PanelConfig());
            }
        });
        panelButtonList.add(buttonConfig);

        var buttonExit = new JButton("종료", new ImageIcon(ResourceManager.getInstance().getImageShutdown()));
        buttonExit.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonExit.setHorizontalTextPosition(SwingConstants.CENTER);
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
    public String toString() {
        return "PanelIntro";
    }
}
