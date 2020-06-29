package assignment.panel;

import assignment.Config;
import assignment.Program;
import assignment.game.object.AIPlayer;
import assignment.game.object.BuildingStory;
import assignment.game.object.CardType;
import assignment.game.object.City;
import assignment.game.object.MarkerColor;
import assignment.game.object.Marker;
import assignment.game.object.MarkerPosition;
import assignment.game.object.NetPlayer;
import assignment.game.object.Player;
import assignment.window.MainWindow;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InGamePanel extends JPanel implements IUpdatable {
    private static final int CITY_SIZE = 6;
    private static final int SPOT_ROW_PER_CITY = 2;
    private static final int SPOT_COLUMN_PER_CITY = 3;
    private static final int MAX_PLAYER_SIZE = 4;
    private static final int MAX_SELECTING_BUILDING_COUNT = 6;
    private static final int MAX_ROUND_COUNT = 4;

    private static final String IMG_CAKES_PATH = "resources/images/cakes.png";
    private static BufferedImage sImageCakes;

    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<CardType> mDummyCards = new ArrayList<CardType>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();
    private Player mMyPlayer = new Player(Config.getUserId());
    private int mCurrentRoundCount = 1;
    private JPanel mPanelGridBag;
    private JPanel mPanelUI;

    public InGamePanel(String[] netPlayerIds) {
        locateMarkers(netPlayerIds);
        initializeBoard();
        initializeDummyCards();

        setLayout(new GridLayout(1, 1));
        add(createPanelMain());
    }

    @Override
    public void updateComponents() {

    }

    private void locateMarkers(String[] netPlayerIds) {
        Random rand = new Random(System.currentTimeMillis());

        ArrayList<MarkerColor> colors = new ArrayList<MarkerColor>(Arrays.asList(MarkerColor.values()));
        ArrayList<MarkerPosition> positions = new ArrayList<MarkerPosition>(Arrays.asList(MarkerPosition.values()));

        mPlayers.add(mMyPlayer);
        for (var id : netPlayerIds) {
            mPlayers.add(new NetPlayer(id));
        }
        int computerNum = 0;
        while (mPlayers.size() < MAX_PLAYER_SIZE) {
            mPlayers.add(new AIPlayer("computer" + computerNum++));
        }
        Collections.shuffle(mPlayers);

        final int p = rand.nextInt(positions.size());
        int n = p;

        for (var player : mPlayers) {
            int c = rand.nextInt(colors.size());

            player.setMarker(new Marker(player.getId(), positions.get(n), colors.get(c), positions.get(p)));
            n = (n + 1) % 4;

            colors.remove(c);
        }
    }

    private void initializeDummyCards() {
        for (var card : CardType.values()) {
            for (int i = 0; i < 5; ++i) {
                mDummyCards.add(card);
            }
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < CITY_SIZE; ++i) {
            mCities.add(new City());
        }
    }

    private JPanel createPanelMain() {
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelMain.setBackground(Color.DARK_GRAY);

        mPanelGridBag = new JPanel(new GridBagLayout());
        mPanelGridBag.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.weightx = 1.0;
        mPanelGridBag.add(createPanelBoard(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mPanelGridBag.add(createPanelGameLog(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        //gbc.weightx = 1.0;
        //gbc.weighty = 0.4;
        mPanelGridBag.add(createPanelUI(), gbc);

        panelMain.add(mPanelGridBag);

        return panelMain;
    }

    private JPanel createPanelBoard() {
        JPanel panelBoard = new JPanel(new GridBagLayout());
        //panelBoard.setBackground(Color.PINK);

        JPanel panelMap = new JPanel(new GridLayout(2, 3));
        panelMap.setPreferredSize(new Dimension(486, 324));

        JLayeredPane[] layeredPaneCities = new JLayeredPane[CITY_SIZE];
        for (int i = 0; i < CITY_SIZE; ++i) {
            layeredPaneCities[i] = new JLayeredPane();
            layeredPaneCities[i].setBackground(i % 2 > 0 ? Color.WHITE : Color.GRAY);
            layeredPaneCities[i].setOpaque(true); // transparent

            var city = mCities.get(i);
            var spots = city.getSpots();
            int spotSize = spots.size();

            for (int j = 0; j < spotSize; ++j) {
                var spot = city.getSpots().get(j);

                spot.addTo(layeredPaneCities[i]);
                spot.getLabelSpot().setBounds((j % 3) * 54, (j / 3) * 54, 49, 49);
            }

            panelMap.add(layeredPaneCities[i]);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelBoard.add(panelMap, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelBoard.add(mPlayers.get(0).getMarker().getLayeredPane(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelBoard.add(mPlayers.get(1).getMarker().getLayeredPane(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelBoard.add(mPlayers.get(2).getMarker().getLayeredPane(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelBoard.add(mPlayers.get(3).getMarker().getLayeredPane(), gbc);

        return panelBoard;
    }

    private JPanel createPanelGameLog() {
        JPanel panelGameLog = new JPanel();
        panelGameLog.setBackground(Color.GRAY);
        return panelGameLog;
    }

    private JPanel createPanelUI() {
        mPanelUI = new JPanel();
        mPanelUI.setBackground(Color.white);

        mPanelUI.add(createPanelSelectBuildings());

        return mPanelUI;
    }

    private JPanel createPanelSelectBuildings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        if (sImageCakes == null) {
            try {
                sImageCakes = ImageIO.read(Program.class.getResourceAsStream(IMG_CAKES_PATH));
            } catch (IOException ex) {

            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);

        final int size = BuildingStory.values().length;
        final BuildingStory[] stories = BuildingStory.values();

        gbc.gridy = 0;
        JLabel[] labelPreviewBuildings = new JLabel[size];
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            labelPreviewBuildings[i] = new JLabel(new ImageIcon(sImageCakes.getSubimage(32 * i, 0, 32, 96)));
            panel.add(labelPreviewBuildings[i], gbc);
        }

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("선택 개수"), gbc);
        JSpinner[] spinners = new JSpinner[size];
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            spinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, Math.min(MAX_SELECTING_BUILDING_COUNT, mMyPlayer.getBuildingCount(stories[i])), 1));
            spinners[i].setEditor(new JSpinner.DefaultEditor(spinners[i]));
            panel.add(spinners[i], gbc);
        }

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("잔여 개수"), gbc);
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            panel.add(new JLabel(String.format("%d", mMyPlayer.getBuildingCount(stories[i]))), gbc);
        }

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        panel.add(new JLabel("- 사용할 블록 6개를 선택하세요 -", SwingConstants.CENTER), gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton buttonApply = new JButton(mCurrentRoundCount + "라운드 시작");
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buildingCount = 0;
                for (int i = 0; i < size; ++i) {
                    buildingCount += (Integer) spinners[i].getValue();
                }

                if (buildingCount != MAX_SELECTING_BUILDING_COUNT) {
                    JOptionPane.showMessageDialog(MainWindow.getInstance(), "블록은 반드시 6개를 선택해야 합니다.");
                    return;
                }

                for (int i = 0; i < size; ++i) {
                    int count = (Integer) spinners[i].getValue();
                    for (int c = 0; c < count; ++c) {
                        mMyPlayer.takeBuilding(stories[i]);
                    }
                }

                Object source = e.getSource();
                if (source instanceof Component) {
                    Component comp = (Component) source;
                    var parent = comp.getParent();
                    //parent.setVisible(false);
                    mPanelUI.remove(parent);
                    revalidate();
                    repaint();
                }
            }
        });
        panel.add(buttonApply, gbc);

        return panel;
    }
}
