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
import assignment.utility.AudioManager;
import assignment.utility.ResourceManager;
import assignment.frame.FrameMain;
import assignment.utility.StringUtility;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

public final class PanelInGame extends JPanel {
    private Timer mTimer;
    private String[] netPlayerIds;
    private int mTurnCount;
    private int mLastTurnCount;
    private int mRoundCount;
    private int mStartPlayerIndex;
    private int mLimitTime;
    private GameFlowType mGameFlow = GameFlowType.GAME_START;
    private MyPlayer mMyPlayer = new MyPlayer(Config.getUserId());
    private ArrayList<City> mCities = new ArrayList<City>();
    private ArrayList<CardType> mDummyCards = new ArrayList<CardType>();
    private ArrayList<Player> mPlayers = new ArrayList<Player>();
    private PanelHeadUpDisplay mPanelHUD;
    private PanelLog mPanelLog;

    public PanelInGame(final String[] netPlayerIds) {
        locateMarkers(netPlayerIds);
        initializeDummyCards();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setBackground(Color.WHITE);

        var panelGridBag = new JPanel(new GridBagLayout());
        panelGridBag.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 0.7;
        panelGridBag.add(new PanelGameBoard(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 0.7;
        mPanelLog = new PanelLog();
        panelGridBag.add(mPanelLog, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        mPanelHUD = new PanelHeadUpDisplay();
        panelGridBag.add(mPanelHUD, gbc);

        add(panelGridBag);
    }

    @Override
    public String toString() {
        return "PanelInGame";
    }

    public void start() {
        mTimer = new javax.swing.Timer(Config.TIMER_DELAY, new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                update();
                --mLimitTime;
                mLimitTime = Math.max(0, mLimitTime);
                mPanelHUD.mPanelStatus.updateTime();
            }
        });
        mTimer.start();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(ResourceManager.getInstance().getImageBackground2(), 0, 0, null);
    }

    // 점수 계산 (각 라운드 마다)
    private void calculateScore() {
        // 1. 전체 중 가장 높은 빌딩을 소유한 플레이어 +3 (동점인 경우 아무도 받지 못함)
        {
            int maxHeight = 0;
            HashSet<PlayerPositionType> highestCakeOwnerPositions = new HashSet<PlayerPositionType>();
            for (var city : mCities) {
                for (var spot : city.getSpots()) {
                    int height = spot.getCakeHeight();
                    if (height <= 0 || maxHeight > height) {
                        continue;
                    }

                    if (maxHeight < height) {
                        maxHeight = height;
                        highestCakeOwnerPositions.clear();
                    }
                    highestCakeOwnerPositions.add(spot.getOwnerPosition());
                }
            }

            if (highestCakeOwnerPositions.size() == 1) {
                getPlayerByPosition((PlayerPositionType) highestCakeOwnerPositions.toArray()[0]).addScore(3);
            }
        }

        // 2. 각 도시별로 가장 많은 빌딩을 소유한 플레이어 +2 (동점인 경우 아무도 받지 못함)
        for (var city : mCities) {
            int[] spotCounts = new int[PlayerPositionType.SIZE];
            int maxSpotCount = 0;
            PlayerPositionType p = null;

            for (var spot : city.getSpots()) {
                if (spot.getCakeHeight() <= 0) {
                    continue;
                }

                int index = spot.getOwnerPosition().getIndex();

                if (maxSpotCount < ++spotCounts[index]) {
                    maxSpotCount = spotCounts[index];
                    p = spot.getOwnerPosition();
                }
            }

            int overlappedSpotCount = 0;
            for (var count : spotCounts) {
                if (maxSpotCount == count) {
                    ++overlappedSpotCount;
                }
            }

            if (overlappedSpotCount == 1) {
                getPlayerByPosition(p).addScore(2);
            }
        }

        // 3. 각 플레이어가 소유한 빌딩 1채 당 +1
        for (var city : mCities) {
            for (var spot : city.getSpots()) {
                if (spot.getCakeHeight() <= 0) {
                    continue;
                }

                getPlayerByPosition(spot.getOwnerPosition()).addScore(1);
            }
        }
    }

    private Player getPlayerByPosition(final PlayerPositionType playerPositionType) {
        for (var player : mPlayers) {
            if (player.getPosition() == playerPositionType) {
                return player;
            }
        }
        return null;
    }

    private Player getWinner() {
        // 4. 라운드를 마치고 가장 높은 점수를 얻은 플레이어가 승리
        int maxScore = 0;
        for (var player : mPlayers) {
            if (maxScore < player.getScore()) {
                maxScore = player.getScore();
            }
        }

        ArrayList<Player> highestScorePlayers = new ArrayList<Player>();
        for (var player : mPlayers) {
            if (maxScore == player.getScore()) {
                highestScorePlayers.add(player);
            }
        }

        if (highestScorePlayers.size() == 1) {
            return highestScorePlayers.get(0);
        }

        // 5. 동점이면 가장 높은 빌딩을 소유한 플레이어가 승리
        int maxHeight = 0;
        HashSet<PlayerPositionType> highestCakeOwnerPositions = new HashSet<PlayerPositionType>();
        for (var city : mCities) {
            for (var spot : city.getSpots()) {
                int height = spot.getCakeHeight();
                if (height <= 0 || maxHeight > height) {
                    continue;
                }

                if (maxHeight < height) {
                    maxHeight = height;
                    highestCakeOwnerPositions.clear();
                }
                highestCakeOwnerPositions.add(spot.getOwnerPosition());
            }
        }

        if (highestCakeOwnerPositions.size() == 1) {
            return getPlayerByPosition((PlayerPositionType) highestCakeOwnerPositions.toArray()[0]);
        }

        // 6. 그래도 동점이면 가장 많은 빌딩을 소유한 플레이어가 승리
        int[] counts = new int[PlayerPositionType.SIZE];
        for (var positionType : highestCakeOwnerPositions) {
            int index = positionType.getIndex();

            for (var city : mCities) {
                for (var spot : city.getSpots()) {
                    if (spot.getCakeHeight() <= 0) {
                        continue;
                    }

                    if (spot.getOwnerPosition() == positionType) {
                        ++counts[index];
                    }
                }
            }
        }

        int maxCount = 0;
        for (var positionType : highestCakeOwnerPositions) {
            int index = positionType.getIndex();
            if (maxCount < counts[index]) {
                maxCount = counts[index];
            }
        }

        for (var positionType : highestCakeOwnerPositions) {
            int index = positionType.getIndex();
            if (maxCount == counts[index]) {
                return getPlayerByPosition(positionType);
            }
        }

        return null;
    }

    private void locateMarkers(final String[] netPlayerIds) {
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

    private void update() {
        switch (mGameFlow) {
            case GAME_START:
                mPanelLog.println("환영합니다.");

                Random random = new Random(System.currentTimeMillis());
                mStartPlayerIndex = random.nextInt(Config.MAX_PLAYER_SIZE);

                // 랜덤 카드 4장씩 분배
                for (var player : mPlayers) {
                    for (int i = 0; i < Config.ROUND_CARD_COUNT; ++i) {
                        player.takeCardFromDummy(mDummyCards);
                    }
                }
                mPanelLog.println(String.format("각각 %d장의 카드를 받았습니다.", Config.ROUND_CARD_COUNT));

                mGameFlow = GameFlowType.NEW_ROUND;
                break;

            case NEW_ROUND:
                if (mRoundCount > 0) {
                    calculateScore();
                    mPanelLog.printScores();
                }

                if (mRoundCount >= Config.MAX_ROUND_COUNT) {
                    mGameFlow = GameFlowType.GAME_OVER;

                    return;
                }

                mTurnCount = 0;
                mLastTurnCount = -1;

                mStartPlayerIndex = (mStartPlayerIndex + 1) % Config.MAX_PLAYER_SIZE;

                mPanelLog.println(String.format("%d 라운드 시작합니다.", ++mRoundCount));
                mPanelLog.println(String.format("각자 냉장고에서 케익 %d개를 꺼내세요.", Config.MAX_SELECTING_CAKE_COUNT));
                mPanelHUD.mPanelCakeFridge.setEnabled(true);

                AudioManager.play("bell.wav");

                mGameFlow = GameFlowType.CHOOSE_AI_PLAYER_SIX_CAKES;
                break;

            case CHOOSE_AI_PLAYER_SIX_CAKES:
                mMyPlayer.setCardSelected(false);
                mMyPlayer.setCakeSelected(false);

                // AIPlayer 케이크 6개 선택
                for (var player : mPlayers) {
                    if (player == mMyPlayer) {
                        continue;
                    }

                    var ai = (AIPlayer) player;
                    for (int i = 0; i < Config.MAX_SELECTING_CAKE_COUNT; ++i) {
                        ai.takeRandomCake();
                    }
                }

                mGameFlow = GameFlowType.CHOOSE_MY_PLAYER_SIX_CAKES;
                mLimitTime = Config.getLimitSecondsPerTurn();
                break;

            case CHOOSE_MY_PLAYER_SIX_CAKES:
                if (mLimitTime <= 0) {
                    for (int i = 0; i < Config.MAX_SELECTING_CAKE_COUNT; ++i) {
                        mMyPlayer.takeRandomCake();
                    }
                    mPanelHUD.mPanelCakeFridge.setEnabled(false);
                    mPanelHUD.mPanelCakeFridge.update();
                }

                if (mMyPlayer.isCakeSelectingFinished()) {

                    mPanelLog.println(String.format("%s 님부터 시작합니다.", mPlayers.get(mStartPlayerIndex).getId()));
                    mPanelHUD.mPanelStatus.update();

                    mGameFlow = GameFlowType.USE_CARD_AND_CAKE;
                }

                break;

            case USE_CARD_AND_CAKE:
                if (mTurnCount >= Config.MAX_PLAYER_SIZE * Config.MAX_SELECTING_CAKE_COUNT) {
                    mTurnCount = 0;
                    mGameFlow = GameFlowType.NEW_ROUND;

                    return;
                }

                mPanelHUD.mPanelStatus.update();
                int index = (mStartPlayerIndex + mTurnCount) % Config.MAX_PLAYER_SIZE;
                var targetPlayer = mPlayers.get(index);

                if (targetPlayer instanceof MyPlayer) {
                    if (mLastTurnCount != mTurnCount) {
                        mLastTurnCount = mTurnCount;

                        mMyPlayer.setCardSelected(false);
                        mMyPlayer.setCakeSelected(false);

                        mPanelLog.println("당신의 차례입니다.");
                        mLimitTime = Config.getLimitSecondsPerTurn();

                        mPanelHUD.mPanelCardList.setEnabled(true);
                        mPanelHUD.mPanelCakeList.setEnabled(false);
                    }

                    if (mLimitTime <= 0 && !mMyPlayer.isCardSelected()) {
                        var card = mMyPlayer.pickUpRandomCard();
                        mDummyCards.add(card);

                        mMyPlayer.setCardSelected(true);
                        mMyPlayer.setNowCard(card);

                        mLimitTime = Config.getLimitSecondsPerTurn();
                    }

                    if (!mMyPlayer.isCardSelected()) {
                        return;
                    }

                    if (mLimitTime <= 0 && !mMyPlayer.isCakeSelected()) {
                        var card = mMyPlayer.getNowCard();

                        ArrayList<Spot> targetSpots = new ArrayList<Spot>();
                        ArrayList<Cake> targetCakes = new ArrayList<Cake>();
                        for (var city : mCities) {
                            var spot = city.getSpotByCake(card, mMyPlayer.getPosition());
                            for (var cake : mMyPlayer.getUsableCakes()) {
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

                        mMyPlayer.useCake(targetCake);

                        for (var city : mCities) {
                            for (var spot : city.getSpots()) {
                                spot.clearColor();
                            }
                        }
                        targetSpot.updateColor(mMyPlayer);

                        mMyPlayer.setCakeSelected(true);
                    }

                    mPanelHUD.mPanelCardList.setEnabled(false);
                    mPanelHUD.mPanelCakeList.setEnabled(true);

                    if (!mMyPlayer.isCakeSelected()) {
                        return;
                    }

                    mPanelHUD.mPanelCakeList.setEnabled(false);

                } else if (targetPlayer instanceof NetPlayer) {

                } else if (targetPlayer instanceof AIPlayer) {

                    var ai = (AIPlayer) targetPlayer;
                    var card = ai.pickUpRandomCard();
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
                    ai.useCake(targetCake);

                    for (var city : mCities) {
                        for (var spot : city.getSpots()) {
                            spot.clearColor();
                        }
                    }
                    targetSpot.updateColor(ai);

                } else {
                    assert (false) : "Invalid Player";
                }

                mPanelLog.println(String.format("%s 님이 케익을 놓았습니다.", targetPlayer.getId()));

                targetPlayer.takeCardFromDummy(mDummyCards);
                mPanelLog.println(String.format("%s 님이 카드를 1장 가져갑니다.", targetPlayer.getId()));

                ++mTurnCount;
                AudioManager.play("turn.wav");

                break;

            case GAME_OVER:
                Player winner = getWinner();
                if (winner == mMyPlayer) {
                    AudioManager.play("tada.wav");
                } else {
                    AudioManager.play("lose.wav");
                }

                mPanelLog.println("- 게임 끝 -");
                mPanelLog.println(String.format("우승자 : %s", winner.getId()));

                mTimer.stop();
                break;

            default:
                assert (false);
        }
    }

    private class PanelGameBoard extends JPanel {
        public PanelGameBoard() {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel panelMap = new JPanel(new GridLayout(2, 3)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    Graphics2D graphics = (Graphics2D) g;
                    Dimension arcs = new Dimension(15, 15);

                    int width = getWidth();
                    int height = getHeight();

                    float dash1[] = { 10.0f };
                    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
                    graphics.setStroke(dashed);

                    graphics.setColor(new Color(0x2c3d4f));
                    graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
                }
            };
            panelMap.setOpaque(false);
            panelMap.setPreferredSize(new Dimension(480, 324));

            for (int i = 0; i < Config.MAX_CITY_SIZE; ++i) {
                var city = new City();
                mCities.add(city);
                panelMap.add(city.getLayeredPane());

                for (var spot : city.getSpots()) {
                    spot.getLabelTarget().addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!mMyPlayer.isCardSelected() || mMyPlayer.isCakeSelected()) {
                                return;
                            }

                            int selectedIndex = mPanelHUD.getPanelCakeList().getListCake().getSelectedIndex();
                            if (selectedIndex < 0) {

                                return;
                            }

                            var cake = mMyPlayer.getUsableCakes().get(selectedIndex);

                            if (!spot.isStackable(cake)) {
                                JOptionPane.showMessageDialog(null, "이곳에 케익을 둘 수 없습니다.", FrameMain.getInstance().getTitle(), JOptionPane.ERROR_MESSAGE);

                                return;
                            }

                            int result = JOptionPane.showConfirmDialog(FrameMain.getInstance(), "이곳에 케익을 두시겠습니까?", FrameMain.getInstance().getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (result != JOptionPane.YES_OPTION) {
                                return;
                            }

                            for (var city : mCities) {
                                city.clearTargetImages();
                            }

                            mMyPlayer.useCake(cake);
                            mMyPlayer.setCakeSelected(true);

                            mPanelHUD.mPanelCakeList.mListCake.setSelectedIndex(-1);

                            spot.stackCake(cake);
                            spot.updateLabels();

                            for (var city : mCities) {
                                for (var spot : city.getSpots()) {
                                    spot.clearColor();
                                }
                            }
                            spot.updateColor(mMyPlayer);
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
            gbc.anchor = GridBagConstraints.PAGE_START;
            add(mPlayers.get(0).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(mPlayers.get(1).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.PAGE_END;
            add(mPlayers.get(2).getMarker().getLayeredPane(), gbc);

            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            add(mPlayers.get(3).getMarker().getLayeredPane(), gbc);
        }
    }

    private class PanelLog extends JPanel {
        private JTextArea mTextAreaLog;

        public PanelLog() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(200, getHeight()));
            setBorder(BorderFactory.createTitledBorder("로그 메세지"));
            setOpaque(false);

            mTextAreaLog = new JTextArea();
            DefaultCaret caret = (DefaultCaret) mTextAreaLog.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            mTextAreaLog.setEditable(false);

            JScrollPane scroller = new JScrollPane(mTextAreaLog);
            add(scroller, BorderLayout.CENTER);
        }

        public void print(String text) {
            mTextAreaLog.append(text);
        }

        public void println(String text) {
            mTextAreaLog.append(text);
            mTextAreaLog.append(System.lineSeparator());
        }

        private void printScores() {
            println(StringUtility.EMPTY);
            println("=".repeat(30));
            println(String.format(" * %d 라운드 점수 결과", mRoundCount));
            println("-".repeat(50));
            for (var player : mPlayers) {
                println(String.format(" - %s : %d점", player.getId(), player.getScore()));
            }
            println("=".repeat(30));
            println(StringUtility.EMPTY);
        }

        public void clear() {
            mTextAreaLog.setText(StringUtility.EMPTY);
        }
    }

    private class PanelHeadUpDisplay extends JPanel {
        private PanelCakeFridge mPanelCakeFridge;
        private PanelCakeList mPanelCakeList;
        private PanelCardList mPanelCardList;
        private PanelStatus mPanelStatus;

        public PanelHeadUpDisplay() {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(getWidth(), Config.HUD_HEIGHT));
            setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            mPanelCakeFridge = new PanelCakeFridge();
            add(mPanelCakeFridge, gbc);

            mPanelCakeList = new PanelCakeList();
            add(mPanelCakeList, gbc);

            mPanelCardList = new PanelCardList();
            add(mPanelCardList, gbc);

            mPanelStatus = new PanelStatus();
            add(mPanelStatus, gbc);

            setEnabled(false);
        }

        public PanelCakeFridge getPanelCakeFridge() {
            return mPanelCakeFridge;
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

            mPanelCakeFridge.setEnabled(enabled);
            mPanelCakeList.setEnabled(enabled);
            mPanelCardList.setEnabled(enabled);
        }
    }

    private class PanelCakeFridge extends JPanel {
        private JLabel[] mLabelRemainCakeCounts = new JLabel[CakeLayerType.SIZE];
        private JSpinner[] mSpinners = new JSpinner[CakeLayerType.SIZE];
        private SpinnerNumberModel[] mSpinnerNumberModels = new SpinnerNumberModel[CakeLayerType.SIZE];

        public PanelCakeFridge() {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(208, Config.HUD_HEIGHT));
            setBorder(BorderFactory.createTitledBorder("냉장고"));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2,2,2,2);

            final CakeLayerType[] layers = CakeLayerType.values();

            gbc.gridy = 0;
            JLabel[] labelPreviewCakes = new JLabel[CakeLayerType.SIZE];
            for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                gbc.gridx = i + 1;
                labelPreviewCakes[i] = new JLabel(new ImageIcon(ResourceManager.getInstance().getImageSetCake().getSubimage(Config.CAKE_IMAGE_WIDTH * i, 0, Config.CAKE_IMAGE_WIDTH, Config.CAKE_IMAGE_HEIGHT)));
                add(labelPreviewCakes[i], gbc);
            }

            gbc.gridy = 1;
            gbc.gridx = 0;
            add(new JLabel("선택 개수"), gbc);

            for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                gbc.gridx = i + 1;
                mSpinnerNumberModels[i] = new SpinnerNumberModel(0, 0, Math.min(Config.MAX_SELECTING_CAKE_COUNT, mMyPlayer.getCakeLayerCount(layers[i])), 1);
                mSpinners[i] = new JSpinner(mSpinnerNumberModels[i]);
                mSpinners[i].setEditor(new JSpinner.DefaultEditor(mSpinners[i]));
                add(mSpinners[i], gbc);
            }

            gbc.gridy = 2;
            gbc.gridx = 0;
            add(new JLabel("잔여 개수"), gbc);
            for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                gbc.gridx = i + 1;
                mLabelRemainCakeCounts[i] = new JLabel();
                add(mLabelRemainCakeCounts[i], gbc);
            }

            update();

            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 5;
            add(new JLabel("- 사용할 케익 6개를 선택하세요 -", SwingConstants.CENTER), gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            gbc.gridwidth = 5;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            var buttonApply = new JButton("케익 꺼내기");

            buttonApply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int cakeCount = 0;
                    for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                        cakeCount += (Integer) mSpinners[i].getValue();
                    }

                    if (cakeCount != Config.MAX_SELECTING_CAKE_COUNT) {
                        JOptionPane.showMessageDialog(FrameMain.getInstance(), String.format("케익은 반드시 %d개를 선택해야 합니다.", Config.MAX_SELECTING_CAKE_COUNT), FrameMain.getInstance().getTitle(), JOptionPane.YES_OPTION | JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                        int count = (Integer) mSpinners[i].getValue();
                        for (int c = 0; c < count; ++c) {
                            mMyPlayer.takeCake(layers[i]);
                        }
                    }

                    update();
                    setEnabled(false);
                }
            });
            add(buttonApply, gbc);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents()) {
                component.setEnabled(enabled);
            }
        }

        private void update() {
            final CakeLayerType[] layers = CakeLayerType.values();

            for (int i = 0; i < CakeLayerType.SIZE; ++i) {
                mLabelRemainCakeCounts[i].setText(String.format("%d", mMyPlayer.getCakeLayerCount(layers[i])));
                mSpinnerNumberModels[i] = new SpinnerNumberModel(0, 0, Math.min(Config.MAX_SELECTING_CAKE_COUNT, mMyPlayer.getCakeLayerCount(layers[i])), 1);
                mSpinners[i].setValue(0);
                mSpinners[i].setModel(mSpinnerNumberModels[i]);
            }
        }
    }

    private class PanelCakeList extends JPanel {
        private JList<ImageIcon> mListCake;
        private JScrollPane mListCakeScroller;

        public PanelCakeList() {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(230, Config.HUD_HEIGHT));
            setBorder(BorderFactory.createTitledBorder("보유중인 케익"));

            mListCake = new JList<ImageIcon>(mMyPlayer.getModelUsableCakeImages());
            mListCake.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            mListCake.setVisibleRowCount(-1);
            mListCake.setLayoutOrientation(JList.HORIZONTAL_WRAP);

            mListCakeScroller = new JScrollPane();
            mListCakeScroller.setViewportView(mListCake);
            mListCakeScroller.setPreferredSize(new Dimension(210, 108));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            add(mListCakeScroller, gbc);
        }

        public JList<ImageIcon> getListCake() {
            return mListCake;
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents()) {
                component.setEnabled(enabled);
            }

            mListCakeScroller.getHorizontalScrollBar().setEnabled(enabled);
            mListCakeScroller.getVerticalScrollBar().setEnabled(enabled);
            mListCakeScroller.getViewport().getView().setEnabled(enabled);
        }
    }

    private class PanelCardList extends JPanel {
        private JList<ImageIcon> mListCard;
        private JButton mButtonPlayCard;
        private JScrollPane mListCardScroller;

        public PanelCardList() {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setPreferredSize(new Dimension(280, Config.HUD_HEIGHT));
            setBorder(BorderFactory.createTitledBorder("보유중인 카드"));

            mListCard = new JList<ImageIcon>(mMyPlayer.getModelCardImages());
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
                        city.clearTargetImages();
                        city.drawTargetImages(card, mMyPlayer.getPosition());
                    }
                }
            });

            mListCardScroller = new JScrollPane();
            mListCardScroller.setViewportView(mListCard);
            mListCardScroller.setPreferredSize(new Dimension(300, 72));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            add(mListCardScroller, gbc);

            mButtonPlayCard = new JButton("선택한 카드 내기");
            mButtonPlayCard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedIndex = mListCard.getSelectedIndex();
                    if (selectedIndex < 0) {
                        JOptionPane.showMessageDialog(FrameMain.getInstance(), "카드를 선택하세요.", FrameMain.getInstance().getTitle(), JOptionPane.ERROR_MESSAGE);

                        return;
                    }

                    var selectedCard = mMyPlayer.getCards().get(selectedIndex);
                    for (var city : mCities) {
                        city.clearTargetImages();
                        city.drawTargetImages(selectedCard, mMyPlayer.getPosition());
                    }

                    mMyPlayer.pickUpCard(selectedCard);
                    mDummyCards.add(selectedCard);
                    Collections.shuffle(mDummyCards);

                    setEnabled(false);
                    mPanelHUD.mPanelCakeList.setEnabled(true);

                    mListCard.setSelectedIndex(-1);

                    mMyPlayer.setCardSelected(true);
                }
            });
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(mButtonPlayCard, gbc);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            for (var component : getComponents())  {
                component.setEnabled(enabled);
            }

            mListCardScroller.getHorizontalScrollBar().setEnabled(enabled);
            mListCardScroller.getVerticalScrollBar().setEnabled(enabled);
            mListCardScroller.getViewport().getView().setEnabled(enabled);
        }
    }

    private class PanelStatus extends JPanel {
        private JLabel mLabelRound;
        private JLabel mLabelNowPlayer;
        private JLabel mLabelStartPlayer;
        private JLabel mLabelTime;

        public PanelStatus() {
            setLayout(new GridLayout(4, 2));
            setPreferredSize(new Dimension(150, Config.HUD_HEIGHT));
            setOpaque(false);
            setBorder(BorderFactory.createTitledBorder("상태"));

            JLabel labelRound = new JLabel("라운드");
            add(labelRound);
            mLabelRound = new JLabel();
            add(mLabelRound);

            JLabel labelStartPlayer = new JLabel("시작");
            add(labelStartPlayer);
            mLabelStartPlayer = new JLabel();
            add(mLabelStartPlayer);

            JLabel labelNowPlayer = new JLabel("현재 차례");
            add(labelNowPlayer);
            mLabelNowPlayer = new JLabel();
            add(mLabelNowPlayer);

            mLabelTime = new JLabel();
            mLabelTime.setHorizontalAlignment(SwingConstants.CENTER);
            mLabelTime.setFont(new Font(mLabelTime.getFont().getName(), Font.BOLD, 18));
            add(mLabelTime);

            JButton buttonExit = new JButton("나가기");
            buttonExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    var panelManager = PanelManager.getInstance();
                    panelManager.popPanel();
                    panelManager.gotoPanel(new PanelIntro());
                    mTimer.stop();
                }
            });
            add(buttonExit);
        }

        public void update() {
            mLabelRound.setText(String.format("%d", mRoundCount));
            mLabelStartPlayer.setText(mPlayers.get(mStartPlayerIndex).getId());
            mLabelNowPlayer.setText(mPlayers.get((mStartPlayerIndex + mTurnCount) % Config.MAX_PLAYER_SIZE).getId());
        }

        public void updateTime() {
            mLabelTime.setText(String.format("%d초", mLimitTime));
        }
    }
}
