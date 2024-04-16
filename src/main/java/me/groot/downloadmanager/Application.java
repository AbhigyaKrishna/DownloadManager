package me.groot.downloadmanager;

import me.groot.downloadmanager.gui.HistoryPage;
import me.groot.downloadmanager.gui.Screen;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Screen obj = new HistoryPage();
            obj.initialize();
            obj.setVisible(true);
        });
    }
}
