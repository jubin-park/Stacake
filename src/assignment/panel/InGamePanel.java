package assignment.panel;

import assignment.game.object.City;
import assignment.window.MainWindow;
import java.util.ArrayList;
import javax.swing.*;

public class InGamePanel extends JPanel implements IUpdatable  {
    private final MainWindow mMainWindow;

    private static final int CITY_SIZE = 6;
    private static final int SPOT_ROW_PER_CITY = 2;
    private static final int SPOT_COLUMN_PER_CITY = 3;

    private static final int PLAYER_SIZE = 4;

    private ArrayList<City> mCities = new ArrayList<City>();

    public InGamePanel(MainWindow mainWindow) {
        mMainWindow = mainWindow;

        JLabel imgLabel = new JLabel(new ImageIcon("resources/images/spot65-red.png"));
        add(imgLabel);
    }

    @Override
    public void updateComponents() {

    }
}
