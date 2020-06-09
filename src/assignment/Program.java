package assignment;

import assignment.window.MainWindow;

import javax.swing.*;

public class Program {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainWindow mainWindow = new MainWindow(800, 600);
                mainWindow.setVisible(true);
            }
        });
    }
}
