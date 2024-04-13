package me.groot.downloadmanager.gui;

import javax.swing.*;

abstract class Screen extends JFrame {

    public Screen(String title) {
        super(title);
    }

    abstract void initialize();

}
