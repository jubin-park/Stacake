package assignment.panel;

import assignment.game.object.City;
import assignment.game.object.PlayerColorType;
import assignment.game.object.PlayerMarker;
import assignment.window.MainWindow;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class InGamePanel extends JPanel implements IUpdatable {
    private final MainWindow mMainWindow;

    private static final int CITY_SIZE = 6;
    private static final int SPOT_ROW_PER_CITY = 2;
    private static final int SPOT_COLUMN_PER_CITY = 3;
    private static final int PLAYER_SIZE = 4;

    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<PlayerMarker> mPlayerMarkers = new ArrayList<PlayerMarker>();

    public InGamePanel(MainWindow mainWindow) {
        mMainWindow = mainWindow;

        initializeBoard();

        setLayout(new GridLayout(1, 1));

        add(createGridBagPanel());
    }

    @Override
    public void updateComponents() {

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
        panelBoard.setBackground(Color.PINK);

        JPanel panelMap = new JPanel(new GridLayout(2, 3));
        panelMap.setPreferredSize(new Dimension(486, 324));

        JLayeredPane[] layeredPaneCities = new JLayeredPane[CITY_SIZE];
        for (int i = 0; i < CITY_SIZE; ++i) {
            layeredPaneCities[i] = new JLayeredPane();
            layeredPaneCities[i].setBackground(Color.orange);

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

        mPlayerMarkers.add(new PlayerMarker("player0", PlayerColorType.RED));
        mPlayerMarkers.add(new PlayerMarker("player1", PlayerColorType.YELLOW));
        mPlayerMarkers.add(new PlayerMarker("player2", PlayerColorType.GREEN));
        mPlayerMarkers.add(new PlayerMarker("player3", PlayerColorType.BLUE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelBoard.add(panelMap, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelBoard.add(mPlayerMarkers.get(0).getLayeredPane(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelBoard.add(mPlayerMarkers.get(1).getLayeredPane(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panelBoard.add(mPlayerMarkers.get(2).getLayeredPane(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelBoard.add(mPlayerMarkers.get(3).getLayeredPane(), gbc);

        return panelBoard;
    }

    private JPanel createPanelGameLog() {
        JPanel panelGameLog = new JPanel();
        panelGameLog.setBackground(Color.orange);
        return panelGameLog;
    }

    private JPanel createPanelUI() {
        JPanel panelUI = new JPanel();
        panelUI.setBackground(Color.BLUE);
        return panelUI;
    }

    private void initializeBoard() {
        for (int i = 0; i < CITY_SIZE; ++i) {
            mCities.add(new City());
        }
    }
}
