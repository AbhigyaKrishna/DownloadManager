package me.groot.downloadmanager;

import me.groot.downloadmanager.gui.FrontPage;
import me.groot.downloadmanager.gui.HistoryPage;
import me.groot.downloadmanager.gui.Screen;
import me.groot.downloadmanager.gui.SecondPage;

public class Application {
    public static void main(String[] args) {

        Screen obj = new FrontPage();
        obj.initialize();
        obj.setVisible(true);





    }
}
