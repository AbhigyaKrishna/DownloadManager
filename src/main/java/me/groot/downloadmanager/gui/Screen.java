package me.groot.downloadmanager.gui;

import javax.swing.*;

public abstract class Screen extends JFrame {

    public Screen(String title) {
        super(title);
    }

    public abstract void initialize();

}
