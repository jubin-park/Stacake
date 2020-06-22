package assignment.panel;

import assignment.window.MainWindow;
import javax.swing.*;

public class InGamePanel extends JPanel implements IUpdatable  {
    private final MainWindow mMainWindow;

    public InGamePanel(MainWindow mainWindow) {
        mMainWindow = mainWindow;
    }

    @Override
    public void updateComponents() {

    }
}
