package me.groot.downloadmanager;

import me.groot.downloadmanager.database.Database;
import me.groot.downloadmanager.database.DatabaseSettings;
import me.groot.downloadmanager.gui.FrontPage;
import me.groot.downloadmanager.gui.HistoryPage;
import me.groot.downloadmanager.gui.Screen;
import me.groot.downloadmanager.gui.SecondPage;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        Database db = new Database(new DatabaseSettings("database/database","SA",""));
        db.create();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Screen obj = new FrontPage(db);
            obj.initialize();
            obj.setVisible(true);
        });
    }
}
