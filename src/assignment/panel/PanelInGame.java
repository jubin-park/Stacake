package assignment.panel;

import assignment.Config;
import assignment.game.object.AIPlayer;
import assignment.game.object.CakeLayerType;
import assignment.game.object.CardType;
import assignment.game.object.City;
import assignment.game.object.PlayerColorType;
import assignment.game.object.PlayerPositionType;
import assignment.game.object.NetPlayer;
import assignment.game.object.Player;
import assignment.utility.ResourceManager;
import assignment.frame.FrameMain;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelInGame extends JPanel implements IUpdatable {
    private static final int CITY_SIZE = 6;
    private static final int SPOT_ROW_PER_CITY = 2;
    private static final int SPOT_COLUMN_PER_CITY = 3;
    private static final int MAX_PLAYER_SIZE = 4;
    private static final int MAX_SELECTING_BUILDING_COUNT = 6;
    private static final int MAX_ROUND_COUNT = 4;

    private static final int CAKE_IMAGE_WIDTH = 32;
    private static final int CAKE_IMAGE_HEIGHT = 96;

    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<CardType> mDummyCards = new ArrayList<CardType>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();
    private Player mMyPlayer = new Player(Config.getUserId());
    private int mCurrentRoundCount = 1;
    private JPanel mPanelGridBag;
    private JPanel mPanelUI;

    public PanelInGame(String[] netPlayerIds) {
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

        ArrayList<PlayerColorType> colors = new ArrayList<PlayerColorType>(Arrays.asList(PlayerColorType.values()));
        ArrayList<PlayerPositionType> positions = new ArrayList<PlayerPositionType>(Arrays.asList(PlayerPositionType.values()));

        mPlayers.add(mMyPlayer);
        for (var id : netPlayerIds) {
            mPlayers.add(new NetPlayer(id));
        }
        int computerNum = 0;
        while (mPlayers.size() < MAX_PLAYER_SIZE) {
            mPlayers.add(new AIPlayer("computer" + computerNum++));
        }
        Collections.shuffle(mPlayers);

        int positionIndex = 0;
        for (var player : mPlayers) {
            int colorIndex = rand.nextInt(colors.size());
            player.setColor(colors.get(colorIndex));
            colors.remove(colorIndex);
            player.setPosition(positions.get(positionIndex++));
            player.createMarker();
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
        mPanelUI = new JPanel(new BorderLayout());
        mPanelUI.setBackground(Color.white);
        //mPanelUI.setMinimumSize(new Dimension(800, 300));

        // TODO
        // 소유 빌딩 카드
        // 소유 빌딩 블록
        // 이번 라운드의 시작 플레이어
        // 현재 라운드 수
        // 현재 턴의 플레이어 행동
        
        mPanelUI.add(createPanelSelectUsableCakes());

        return mPanelUI;
    }

    private JPanel createPanelCardList() {
        JPanel panelCardList = new JPanel(new FlowLayout());
        panelCardList.setOpaque(false);

        JList<ImageIcon> listCards = new JList<ImageIcon>(mMyPlayer.getModelCardImages());
        listCards.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listCards.setVisibleRowCount(-1);
        listCards.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(listCards);
        listScroller.setPreferredSize(new Dimension(300, 72));

        panelCardList.add(listScroller);

        JButton buttonPlayCard = new JButton("선택한 카드 내기");
        buttonPlayCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = listCards.getSelectedIndex();
                if (selectedIndex < 0) {
                    JOptionPane.showMessageDialog(FrameMain.getInstance(), "블록은 반드시 6개를 선택해야 합니다.");

                    return;
                }

                var selectedCard = mMyPlayer.getCards().get(selectedIndex);

                mMyPlayer.discardCardByIndex(selectedIndex);
                mDummyCards.add(selectedCard);
                Collections.shuffle(mDummyCards);

                buttonPlayCard.setEnabled(false);
            }
        });
        panelCardList.add(buttonPlayCard);

        return panelCardList;
    }

    private JPanel createPanelSelectUsableCakes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);

        final int size = CakeLayerType.values().length;
        final CakeLayerType[] stories = CakeLayerType.values();

        gbc.gridy = 0;
        JLabel[] labelPreviewCakes = new JLabel[size];
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            labelPreviewCakes[i] = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageSetCake().getSubimage(CAKE_IMAGE_WIDTH * i, 0, CAKE_IMAGE_WIDTH, CAKE_IMAGE_HEIGHT)));
            panel.add(labelPreviewCakes[i], gbc);
        }

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("선택 개수"), gbc);
        JSpinner[] spinners = new JSpinner[size];
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            spinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, Math.min(MAX_SELECTING_BUILDING_COUNT, mMyPlayer.getCakeCount(stories[i])), 1));
            spinners[i].setEditor(new JSpinner.DefaultEditor(spinners[i]));
            panel.add(spinners[i], gbc);
        }

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("잔여 개수"), gbc);
        for (int i = 0; i < size; ++i) {
            gbc.gridx = i + 1;
            panel.add(new JLabel(String.format("%d", mMyPlayer.getCakeCount(stories[i]))), gbc);
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
                int cakeCount = 0;
                for (int i = 0; i < size; ++i) {
                    cakeCount += (Integer) spinners[i].getValue();
                }

                if (cakeCount != MAX_SELECTING_BUILDING_COUNT) {
                    JOptionPane.showMessageDialog(FrameMain.getInstance(), "블록은 반드시 6개를 선택해야 합니다.");
                    return;
                }

                for (int i = 0; i < size; ++i) {
                    int count = (Integer) spinners[i].getValue();
                    for (int c = 0; c < count; ++c) {
                        mMyPlayer.takeCake(stories[i]);
                    }
                }

                for (var player : mPlayers) {
                    for (int i = 0; i < 4; ++i) {
                        player.takeCardFromDummy(mDummyCards);
                    }
                }

                mPanelUI.add(createPanelCardList());

                Object source = e.getSource();
                if (source instanceof Component) {
                    Component comp = (Component) source;
                    mPanelUI.remove(comp.getParent());
                    revalidate();
                    repaint();
                }
            }
        });
        panel.add(buttonApply, gbc);

        return panel;
    }
}
