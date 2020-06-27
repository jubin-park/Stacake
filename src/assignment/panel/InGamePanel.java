package assignment.panel;

import assignment.Config;
import assignment.game.object.AIPlayer;
import assignment.game.object.BuildingStoryType;
import assignment.game.object.CardType;
import assignment.game.object.City;
import assignment.game.object.MarkerColor;
import assignment.game.object.Marker;
import assignment.game.object.MarkerPosition;
import assignment.game.object.NetPlayer;
import assignment.game.object.Player;
import assignment.window.MainWindow;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InGamePanel extends JPanel implements IUpdatable {
    private static final int CITY_SIZE = 6;
    private static final int SPOT_ROW_PER_CITY = 2;
    private static final int SPOT_COLUMN_PER_CITY = 3;
    private static final int MAX_PLAYER_SIZE = 4;

    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<CardType> mDummyCards = new ArrayList<CardType>();
    private ArrayList<CardType> mMyRoundCards = new ArrayList<CardType>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();
    private ArrayList<BuildingStoryType> mBuildingBlocks = new ArrayList<BuildingStoryType>();

    public InGamePanel(String[] netPlayerIds) {
        initializePlayers(netPlayerIds);
        initializeBoard();
        initializeDummyCards();
        initializeBuildingBlocks();
        setLayout(new GridLayout(1, 1));
        add(createGridBagPanel());
    }

    @Override
    public void updateComponents() {

    }

    private void initializePlayers(String[] netPlayerIds) {
        Random rand = new Random(System.currentTimeMillis());

        ArrayList<MarkerColor> colors = new ArrayList<MarkerColor>(Arrays.asList(MarkerColor.values()));
        ArrayList<MarkerPosition> positions = new ArrayList<MarkerPosition>(Arrays.asList(MarkerPosition.values()));

        mPlayers.add(new Player(Config.getUserId()));
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

    private void initializeBuildingBlocks() {
        for (int i = 0; i < 2; ++i) {
            mBuildingBlocks.add(BuildingStoryType.FOUR);
        }
        for (int i = 0; i < 4; ++i) {
            mBuildingBlocks.add(BuildingStoryType.THREE);
        }
        for (int i = 0; i < 6; ++i) {
            mBuildingBlocks.add(BuildingStoryType.TWO);
        }
        for (int i = 0; i < 12; ++i) {
            mBuildingBlocks.add(BuildingStoryType.ONE);
        }
    }

    private JPanel createGridBagPanel() {
        JPanel panelMaster = new JPanel(new BorderLayout());
        panelMaster.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelMaster.setBackground(Color.DARK_GRAY);

        JPanel panelGridBag = new JPanel(new GridBagLayout());
        panelGridBag.setOpaque(false); // delete border

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.weightx = 0.5;
        //gbc.weighty = 0.65;
        panelGridBag.add(createPanelBoard(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        panelGridBag.add(createPanelGameLog(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        panelGridBag.add(createPanelUI(), gbc);

        panelMaster.add(panelGridBag);

        return panelMaster;
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
        JPanel panelUI = new JPanel();
        panelUI.setBackground(Color.white);
        return panelUI;
    }

    private void initializeBoard() {
        for (int i = 0; i < CITY_SIZE; ++i) {
            mCities.add(new City());
        }
    }
}
