package assignment.panel;

import assignment.Config;
import assignment.utility.AudioManager;
import assignment.utility.ResourceManager;
import assignment.utility.StringUtility;
import assignment.frame.FrameMain;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PanelConfig extends JPanel {
    private JTextField mTextFieldId = new JTextField(10);
    private JLabel mLabelLimitSecondsPerTurn = new JLabel(StringUtility.EMPTY, SwingConstants.CENTER);
    private JSlider mSliderLimitSecondsPerTurn = new JSlider(10, 30, 30);
    private JLabel mLabelSoundEffectVolume = new JLabel(StringUtility.EMPTY, SwingConstants.CENTER);
    private JSlider mSliderSoundEffectVolume = new JSlider(0, 100, 100);
    
    public PanelConfig() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel panelNested = new JPanel(new GridBagLayout());
        panelNested.setPreferredSize(new Dimension(350, 150));
        panelNested.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(panelNested, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // labels
        JLabel label00 = new JLabel("아이디");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(label00, gbc);

        JLabel label01 = new JLabel("한 턴당 제한 시간");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(label01, gbc);

        JLabel label02 = new JLabel("효과음 볼륨");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(label02, gbc);

        // userId
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(mTextFieldId, gbc);

        // LimitSecondsPerTurn
        mSliderLimitSecondsPerTurn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                byte value = (byte)(((JSlider) (e.getSource())).getValue());
                mLabelLimitSecondsPerTurn.setText(Byte.toString(value) + " 초");
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(mSliderLimitSecondsPerTurn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(mLabelLimitSecondsPerTurn, gbc);

        // SliderSoundEffectVolume
        mSliderSoundEffectVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                byte value = (byte)(((JSlider) (e.getSource())).getValue());
                mLabelSoundEffectVolume.setText(Byte.toString(value) + "%");
            }
        });

        mSliderSoundEffectVolume.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                byte value = (byte)(((JSlider) (e.getSource())).getValue());
                AudioManager.play("tada.wav", value);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(mSliderSoundEffectVolume, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested.add(mLabelSoundEffectVolume, gbc);

        JPanel panelNested2 = new JPanel(new GridBagLayout());
        panelNested2.setPreferredSize(new Dimension(350, 48));
        panelNested2.setBackground(Color.darkGray);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(panelNested2, gbc);

        gbc.fill = GridBagConstraints.BOTH;

        JButton buttonApply = new JButton("적용");
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = mTextFieldId.getText();
                if (text.length() > 0) {
                    PanelManager panelManager = PanelManager.getInstance();
                    panelManager.popPanel();
                    panelManager.gotoPanel(panelManager.getCurrentPanel());
                    Config.setUserId(text);
                    Config.setLimitSecondsPerTurn((byte) mSliderLimitSecondsPerTurn.getValue());
                    Config.setSoundEffectVolume((byte) mSliderSoundEffectVolume.getValue());
                    Config.saveProperties();
                }
                else {
                    JOptionPane.showMessageDialog(FrameMain.getInstance(), "아이디를 입력하세요.");
                }
            }
        });

        JButton buttonCancel = new JButton("취소");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager panelManager = PanelManager.getInstance();
                panelManager.popPanel();
                panelManager.gotoPanel(panelManager.getCurrentPanel());
            }
        });

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested2.add(buttonApply, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        panelNested2.add(buttonCancel, gbc);

        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ResourceManager.getInstance().getImageBackground(), 0, 0, null);
    }

    private void update() {
        mTextFieldId.setText(Config.getUserId());
        mLabelLimitSecondsPerTurn.setText(Byte.toString(Config.getLimitSecondsPerTurn()) + " 초");
        mSliderLimitSecondsPerTurn.setValue(Config.getLimitSecondsPerTurn());
        mLabelSoundEffectVolume.setText(Byte.toString(Config.getSoundEffectVolume()) + " %");
        mSliderSoundEffectVolume.setValue(Config.getSoundEffectVolume());
    }

    @Override
    public String toString() {
        return "PanelConfig";
    }
}
