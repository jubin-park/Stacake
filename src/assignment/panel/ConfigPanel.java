package assignment.panel;

import assignment.Config;
import assignment.utility.StringUtility;
import assignment.window.MainWindow;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigPanel extends JPanel implements IUpdatable {
    private GridBagConstraints mGridBagConstraints = new GridBagConstraints();
    private JTextField mTextFieldId = new JTextField(10);
    private JLabel mLabelLimitSecondsPerTurn = new JLabel(StringUtility.EMPTY, SwingConstants.CENTER);
    private JSlider mSliderLimitSecondsPerTurn = new JSlider(10, 30, 30);
    private JLabel mLabelSoundEffectVolume = new JLabel(StringUtility.EMPTY, SwingConstants.CENTER);
    private JSlider mSliderSoundEffectVolume = new JSlider(0, 100, 100);
    
    public ConfigPanel() {
        // TODO 이미지 버튼으로 구성

        setLayout(new GridBagLayout());

        JPanel panelNested = new JPanel(new GridBagLayout());
        addOnPanel(this, panelNested, 0, 0, 2, 1);
        panelNested.setPreferredSize(new Dimension(350, 150));
        panelNested.setBackground(Color.GRAY);

        mGridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        // labels
        var label00 = new JLabel("아이디");
        var label01 = new JLabel("한 턴당 제한 시간");
        var label02 = new JLabel("효과음 볼륨");
        addOnPanel(panelNested, label00, 0, 0, 1, 1);
        addOnPanel(panelNested, label01, 0, 1, 1, 1);
        addOnPanel(panelNested, label02, 0, 3, 1, 1);

        // userId
        addOnPanel(panelNested, mTextFieldId, 1, 0, 1, 1);

        // LimitSecondsPerTurn
        mSliderLimitSecondsPerTurn.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                byte value = (byte)(((JSlider) (e.getSource())).getValue());
                mLabelLimitSecondsPerTurn.setText(Byte.toString(value) + " 초");
            }
        });
        addOnPanel(panelNested, mSliderLimitSecondsPerTurn, 1, 1, 1, 1);
        addOnPanel(panelNested, mLabelLimitSecondsPerTurn, 1, 2, 1, 1);

        // SliderSoundEffectVolume
        mSliderSoundEffectVolume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                byte value = (byte)(((JSlider) (e.getSource())).getValue());
                mLabelSoundEffectVolume.setText(Byte.toString(value) + "%");
            }
        });
        addOnPanel(panelNested, mSliderSoundEffectVolume, 1, 3, 1, 1);
        addOnPanel(panelNested, mLabelSoundEffectVolume, 1, 4, 1, 1);

        JPanel panelNested2 = new JPanel(new GridBagLayout());
        addOnPanel(this, panelNested2, 0, 1, 2, 1);
        panelNested2.setPreferredSize(new Dimension(350, 48));
        panelNested2.setBackground(Color.darkGray);

        mGridBagConstraints.fill = GridBagConstraints.BOTH;

        var buttonApply = new JButton("적용");
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = mTextFieldId.getText();
                if (text.length() > 0) {
                    PanelManager.getInstance().showPanel(PanelType.INTRO);
                    Config.setUserId(text);
                    Config.setLimitSecondsPerTurn((byte) mSliderLimitSecondsPerTurn.getValue());
                    Config.setSoundEffectVolume((byte) mSliderSoundEffectVolume.getValue());
                    Config.saveProperties();
                }
                else {
                    JOptionPane.showMessageDialog(MainWindow.getInstance(), "아이디를 입력하세요.");
                }
            }
        });

        var buttonCancel = new JButton("취소");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelManager.getInstance().showPanel(PanelType.INTRO);
            }
        });

        mGridBagConstraints.weightx = 1;
        mGridBagConstraints.weighty = 1;
        addOnPanel(panelNested2, buttonApply, 0, 0, 1, 1);
        addOnPanel(panelNested2, buttonCancel, 1, 0, 1, 1);
    }

    @Override
    public void updateComponents() {
        mTextFieldId.setText(Config.getUserId());
        mLabelLimitSecondsPerTurn.setText(Byte.toString(Config.getLimitSecondsPerTurn()) + " 초");
        mSliderLimitSecondsPerTurn.setValue(Config.getLimitSecondsPerTurn());
        mLabelSoundEffectVolume.setText(Byte.toString(Config.getSoundEffectVolume()) + " %");
        mSliderSoundEffectVolume.setValue(Config.getSoundEffectVolume());
    }


    private void addOnPanel(JPanel panel, JComponent component, int gridX, int gridY, int gridWidth, int gridHeight) {
        mGridBagConstraints.gridx = gridX;
        mGridBagConstraints.gridy = gridY;
        mGridBagConstraints.gridwidth = gridWidth;
        mGridBagConstraints.gridheight = gridHeight;
        panel.add(component, mGridBagConstraints);
    }
}
