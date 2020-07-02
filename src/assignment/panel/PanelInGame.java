package assignment.panel;

import assignment.Config;
import assignment.game.GameFlowType;
import assignment.game.object.AIPlayer;
import assignment.game.object.Cake;
import assignment.game.object.CakeLayerType;
import assignment.game.object.CardType;
import assignment.game.object.City;
import assignment.game.object.MyPlayer;
import assignment.game.object.PlayerColorType;
import assignment.game.object.PlayerPositionType;
import assignment.game.object.NetPlayer;
import assignment.game.object.Player;
import assignment.game.object.Spot;
import assignment.utility.ResourceManager;
import assignment.frame.FrameMain;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public final class PanelInGame extends JPanel implements Runnable, IUpdatable {
    private MyPlayer mMyPlayer = new MyPlayer(Config.getUserId());
    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<CardType> mDummyCards = new ArrayList<CardType>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();

    private int mTurnCount;
    private int mRoundCount;
    private int mStartPlayerIndex;

    private GameFlowType mGameFlow = GameFlowType.GAME_START;
    private String[] netPlayerIds;

    private JPanel mPanelGridBag;
    private PanelHeadUpDisplay mPanelHUD;

    public PanelInGame(String[] netPlayerIds) {
        locateMarkers(netPlayerIds);
        initializeDummyCards();

        setLayout(new BorderLayout());
        add(createPanelMain());
    }

    @Override
    public void run() {
        int fps = 1;
        double tickPerSecond = 1_000_000_000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        int maxTurnCount = Config.MAX_PLAYER_SIZE * Config.MAX_ROUND_COUNT;
        var frameMain = FrameMain.getInstance();
        while (frameMain.isRunning() && mTurnCount < maxTurnCount) {
            now = System.nanoTime();
            delta += (now - lastTime) / tickPerSecond;
            lastTime = now;

            if (delta >= 1.0) {
                updateComponents();
                delta--;
            }
        }
    }

    public synchronized void start() {
        var thread = new Thread(this);
        FrameMain.getInstance().setRunning(true);
        thread.start();
    }

    public synchronized void stop() {
        FrameMain.getInstance().setRunning(false);
    }

    @Override
    public synchronized void updateComponents() {
        switch (mGameFlow) {
            case GAME_START:
                System.out.println("환영합니다.");

                // 랜덤 카드 4장씩 분배
                for (var player : mPlayers) {
                    for (int i = 0; i < 4; ++i) {
                        player.takeCardFromDummy(mDummyCards);
                    }
                }

                mGameFlow = GameFlowType.CHOOSE_SIX_CAKES;
                break;

            case CHOOSE_SIX_CAKES:
                // AIPlayer 케이크 6개 선택
                for (var player : mPlayers) {
                    if (player == mMyPlayer || player.isCakeSelectingFinished()) {
                        continue;
                    }

                    assert (player instanceof AIPlayer);

                    var ai = (AIPlayer) player;
                    for (int i = 0; i < Config.MAX_SELECTING_CAKE_COUNT; ++i) {
                        ai.takeRandomCake();
                    }

                    ai.setCakeSelectingFinished(true);
                }

                // 케이크 6개 선택중?
                for (var player : mPlayers) {
                    if (!player.isCakeSelectingFinished()) {
                        return;
                    }
                }

                System.out.println(String.format("%d %s", mRoundCount, "라운드 시작"));

                Random random = new Random(System.currentTimeMillis());
                mStartPlayerIndex = random.nextInt(Config.MAX_PLAYER_SIZE);
                System.out.println(String.format("%s(index=%d) %s", mPlayers.get(mStartPlayerIndex).getId(), mStartPlayerIndex, "님부터 시작합니다."));

                mGameFlow = GameFlowType.USE_CARD_AND_CAKE;
                break;

            case USE_CARD_AND_CAKE:
                if (mTurnCount >= Config.MAX_PLAYER_SIZE) {
                    ++mRoundCount;

                    mTurnCount = 0;
                    for (var player : mPlayers) {
                        player.setCakeSelectingFinished(false);
                    }

                    /*
                    mListCake.setEnabled(true);
                    mListCard.setEnabled(true);
                    mButtonPlayCard.setEnabled(true);

                     */

                    if (mRoundCount >= Config.MAX_ROUND_COUNT) {
                        mGameFlow = GameFlowType.GAME_OVER;
                        return;
                    }

                    mGameFlow = GameFlowType.CHOOSE_SIX_CAKES;

                    return;
                }

                int index = (mStartPlayerIndex + mTurnCount) % Config.MAX_PLAYER_SIZE;
                var targetPlayer = mPlayers.get(index);
                /*
                if (targetPlayer == mMyPlayer) {
                    mListCake.setEnabled(true);
                    mListCard.setEnabled(true);
                    mButtonPlayCard.setEnabled(true);
                    return;
                } else {
                    mListCake.setEnabled(false);
                    mListCard.setEnabled(false);
                    mButtonPlayCard.setEnabled(false);
                }
*/
                var ai = (AIPlayer) targetPlayer;
                var card = ai.useRandomCard();
                mDummyCards.add(card);

                ArrayList<Spot> targetSpots = new ArrayList<Spot>();
                ArrayList<Cake> targetCakes = new ArrayList<Cake>();
                for (var city : mCities) {
                    var spot = city.getSpotByCake(card, ai.getPosition());
                    for (var cake : ai.getUsableCakes()) {
                        if (spot.isStackable(cake)) {
                            targetSpots.add(spot);
                            targetCakes.add(cake);
                        }
                    }
                }

                Random random2 = new Random(System.currentTimeMillis());
                int index2 = random2.nextInt(targetSpots.size());
                var targetSpot = targetSpots.get(index2);
                var targetCake = targetCakes.get(index2);

                targetSpot.stackCake(targetCake);
                targetSpot.updateLabels();
                for (var city : mCities) {
                    for (var spot : city.getSpots()) {
                        spot.updateSpotColor(ai);
                    }
                }

                ++mTurnCount;

                break;

            case GAME_OVER:
                break;

            default:
                assert (false);
        }
    }

    private synchronized void locateMarkers(String[] netPlayerIds) {
        Random rand = new Random(System.currentTimeMillis());

        ArrayList<PlayerColorType> colors = new ArrayList<PlayerColorType>(Arrays.asList(PlayerColorType.values()));
        ArrayList<PlayerPositionType> positions = new ArrayList<PlayerPositionType>(Arrays.asList(PlayerPositionType.values()));

        mPlayers.add(mMyPlayer);
        for (var id : netPlayerIds) {
            mPlayers.add(new NetPlayer(id));
        }
        int computerNum = 0;
        while (mPlayers.size() < Config.MAX_PLAYER_SIZE) {
            mPlayers.add(new AIPlayer("computer" + computerNum++));
        }
        Collections.shuffle(mPlayers);

        int positionIndex = 0;
        for (var player : mPlayers) {
            int colorIndex = rand.nextInt(colors.size());
            player.setColor(colors.get(colorIndex));
            colors.remove(colorIndex);
            player.setPosition(positions.get(positionIndex++));
            player.initializeCakes();
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
        gbc.weightx = 0.7;
        gbc.weighty = 0.7;
        mPanelGridBag.add(new PanelGameBoard(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0.7;
        mPanelGridBag.add(new PanelGameLog(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        mPanelGridBag.add(new PanelHeadUpDisplay(), gbc);

        panelMain.add(mPanelGridBag);

        return panelMain;
    }

    private class PanelGameBoard extends JPanel {
        public PanelGameBoard() {
            setLayout(new GridBagLayout());
            setBackground(Color.ORANGE);

            JPanel panelMap = new JPanel(new GridLayout(2, 3));
            panelMap.setPreferredSize(new Dimension(480, 324));
            panelMap.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            for (int i = 0; i < Config.MAX_CITY_SIZE; ++i) {
                var city = new City();
                mCities.add(city);
                panelMap.add(city.getLayeredPane());

                for (var spot : city.getSpots()) {
                    spot.getLabelTarget().addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int selectedIndex = mPanelHUD.getPanelCakeList().getListCake().getSelectedIndex();
                            if (selectedIndex < 0) {
                                // TODO 카드 제출 후 사용 가능하도록

                                return;
                            }

                            var cake = mMyPlayer.getUsableCakes().get(selectedIndex);

                            if (!spot.isStackable(cake)) {
                                JOptionPane.showConfirmDialog(FrameMain.getInstance(), "이곳에 케익을 둘 수 없습니다.", FrameMain.getInstance().getTitle(), JOptionPane.ERROR_MESSAGE);

                                return;
                            }

                            int result = JOptionPane.showConfirmDialog(FrameMain.getInstance(), "이곳에 케익을 두시겠습니까?", FrameMain.getInstance().getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (result != JOptionPane.YES_OPTION) {
                                return;
                            }

                            mMyPlayer.useCake(cake);

                            //mListCake.setEnabled(false);
                            //mPanelHUD.getPanelCakeList().getListCake().setSelectedIndex(-1);

                            spot.stackCake(cake);
                            spot.updateLabels();
                            spot.updateSpotColor(mMyPlayer);

                            ++mTurnCount;
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            FrameMain.getInstance().setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            FrameMain.getInstance().setCursor(Cursor.getDefaultCursor());
                        }
                    });
                }
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            gbc.gridx = 1;
            gbc.gridy = 1;
            add(panelMap, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.VERTICAL;
            add(mPlayers.get(0).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(mPlayers.get(1).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.VERTICAL;
            add(mPlayers.get(2).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(mPlayers.get(3).getMarker().getLayeredPane(), gbc);
        }
    }

    private class PanelGameLog extends JPanel {
        public PanelGameLog() {
            setBackground(Color.BLUE);
            //setOpaque(false);
        }
    }

    private class PanelHeadUpDisplay extends JPanel {
        private PanelSelectUsableCake mPanelSelectUsableCake;
        private PanelCakeList mPanelCakeList;
        private PanelCardList mPanelCardList;
        private PanelStatus mPanelStatus;

        public PanelHeadUpDisplay() {
            setLayout(new GridLayout(1, 4));
            setBackground(Color.PINK);
            setPreferredSize(new Dimension(getWidth(), 200));
            //setOpaque(false);

            // TODO
            // 소유 빌딩 카드
            // 소유 빌딩 블록
            // 이번 라운드의 시작 플레이어
            // 현재 라운드 수
            // 현재 턴의 플레이어 행동

            mPanelSelectUsableCake = new PanelSelectUsableCake();
            mPanelCakeList = new PanelCakeList();
            mPanelCardList = new PanelCardList();
            mPanelStatus = new PanelStatus();

            add(mPanelSelectUsableCake);
            add(mPanelCakeList);
            add(mPanelCardList);
            add(mPanelStatus);
        }

        public PanelSelectUsableCake getPanelSelectUsableCake() {
            return mPanelSelectUsableCake;
        }

        public PanelCakeList getPanelCakeList() {
            return mPanelCakeList;
        }

        public PanelCardList getPanelCardList() {
            return mPanelCardList;
        }

        public PanelStatus getPanelStatus() {
            return mPanelStatus;
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

        }
    }

    private class PanelSelectUsableCake extends JPanel {
        public PanelSelectUsableCake() {
            setLayout(new GridBagLayout());
            setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2,2,2,2);

            final int size = CakeLayerType.values().length;
            final CakeLayerType[] layers = CakeLayerType.values();

            gbc.gridy = 0;
            JLabel[] labelPreviewCakes = new JLabel[size];
            for (int i = 0; i < size; ++i) {
                gbc.gridx = i + 1;
                labelPreviewCakes[i] = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * i, 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT)));
                add(labelPreviewCakes[i], gbc);
            }

            gbc.gridy = 1;
            gbc.gridx = 0;
            add(new JLabel("선택 개수"), gbc);
            JSpinner[] spinners = new JSpinner[size];
            for (int i = 0; i < size; ++i) {
                gbc.gridx = i + 1;
                spinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, Math.min(Config.MAX_SELECTING_CAKE_COUNT, mMyPlayer.getCakeCount(layers[i])), 1));
                spinners[i].setEditor(new JSpinner.DefaultEditor(spinners[i]));
                add(spinners[i], gbc);
            }

            gbc.gridy = 2;
            gbc.gridx = 0;
            add(new JLabel("잔여 개수"), gbc);
            for (int i = 0; i < size; ++i) {
                gbc.gridx = i + 1;
                add(new JLabel(String.format("%d", mMyPlayer.getCakeCount(layers[i]))), gbc);
            }

            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 5;
            add(new JLabel("- 사용할 블록 6개를 선택하세요 -", SwingConstants.CENTER), gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            gbc.gridwidth = 5;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            var buttonApply = new JButton(String.format("%d %s", (mTurnCount) / 4 + 1, "라운드 시작"));
            buttonApply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int cakeCount = 0;
                    for (int i = 0; i < size; ++i) {
                        cakeCount += (Integer) spinners[i].getValue();
                    }

                    if (cakeCount != Config.MAX_SELECTING_CAKE_COUNT) {
                        JOptionPane.showMessageDialog(FrameMain.getInstance(), "블록은 반드시 6개를 선택해야 합니다.", FrameMain.getInstance().getTitle(), JOptionPane.YES_OPTION | JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    mMyPlayer.setCakeSelectingFinished(true);

                    for (int i = 0; i < size; ++i) {
                        int count = (Integer) spinners[i].getValue();
                        for (int c = 0; c < count; ++c) {
                            mMyPlayer.takeCake(layers[i]);
                        }
                    }

                    /*
                    mPanelSelectUsableCake.setEnabled(false);
                    mPanelCardList.setEnabled(true);


                    mPanelHUD.revalidate();
                    mPanelHUD.repaint();
 */
                /*
                Object source = e.getSource();
                if (source instanceof Component) {
                    Component comp = (Component) source;
                    mPanelHUD.remove(comp.getParent());
                    revalidate();
                    repaint();
                }
                */
                }
            });
            add(buttonApply, gbc);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents() ){
                component.setEnabled(false);
            }
        }
    }

    private class PanelCakeList extends JPanel {
        private JList<ImageIcon> mListCake;

        public PanelCakeList() {
            setOpaque(false);
            mListCake = new JList<ImageIcon>(mMyPlayer.getModelUsableCakeImages());
            mListCake.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            mListCake.setVisibleRowCount(-1);
            mListCake.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            mListCake.setEnabled(false);

            JScrollPane listCakeScroller = new JScrollPane();
            listCakeScroller.setViewportView(mListCake);
            listCakeScroller.setPreferredSize(new Dimension(240, 108));

            add(listCakeScroller);
        }

        public JList<ImageIcon> getListCake() {
            return mListCake;
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents() ){
                component.setEnabled(false);
            }
        }
    }

    private class PanelCardList extends JPanel {
        private JList<ImageIcon> mListCard;
        private JButton mButtonPlayCard;

        public PanelCardList() {
            setLayout(new FlowLayout());
            setOpaque(false);

            mListCard = new JList<ImageIcon>(mMyPlayer.getModelCardImages());
            mListCard.setEnabled(false);
            mListCard.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            mListCard.setVisibleRowCount(-1);
            mListCard.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            mListCard.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    int selectedIndex = mListCard.getSelectedIndex();
                    if (selectedIndex < 0) {

                        return;
                    }

                    var card = mMyPlayer.getCards().get(selectedIndex);
                    for (var city : mCities) {
                        city.updatePreview(card, mMyPlayer.getPosition());
                    }
                }
            });

            JScrollPane mListCardScroller = new JScrollPane();
            mListCardScroller.setViewportView(mListCard);
            mListCardScroller.setPreferredSize(new Dimension(300, 72));

            add(mListCardScroller);

            mButtonPlayCard = new JButton("선택한 카드 내기");
            mButtonPlayCard.setEnabled(false);
            mButtonPlayCard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedIndex = mListCard.getSelectedIndex();
                    if (selectedIndex < 0) {
                        JOptionPane.showMessageDialog(FrameMain.getInstance(), "카드를 선택하세요.", FrameMain.getInstance().getTitle(), JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    var selectedCard = mMyPlayer.getCards().get(selectedIndex);

                    mMyPlayer.useCard(selectedCard);
                    mDummyCards.add(selectedCard);
                    Collections.shuffle(mDummyCards);

                    mButtonPlayCard.setEnabled(false);

                    mListCard.setSelectedIndex(-1);
                    mListCard.setEnabled(false);
                }
            });
            add(mButtonPlayCard);

        }

        public JList<ImageIcon> getListCard() {
            return mListCard;
        }

        public JButton getButtonPlayCard() {
            return mButtonPlayCard;
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents() ){
                component.setEnabled(false);
            }
        }
    }

    private class PanelStatus extends JPanel {
        private JLabel mLabelRound;
        private JLabel mLabelNowPlayer;
        private JLabel mLabelStartPlayer;
        private JLabel mLabelPlayerAction;

        public PanelStatus() {
            setLayout(new GridLayout(4, 1));

            mLabelRound = new JLabel("1 라운드");
            add(mLabelRound);

            mLabelNowPlayer = new JLabel("현재 차례");
            add(mLabelNowPlayer);

            mLabelStartPlayer = new JLabel("시작 플레이어");
            add(mLabelStartPlayer);

            mLabelPlayerAction = new JLabel("현재 턴의 플레이어 행동");
            add(mLabelPlayerAction);
        }

        public void update() {

        }


    }
}
