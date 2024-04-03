package me.groot.downloadmanager.gui;
import javax.swing.*;
import java.awt.*;

public class FrontPage {
    public FrontPage(){
        JFrame frame = new JFrame("Download Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JTextField linkbox = new JTextField(32);
        //linkbox.setSize(1600, 1600);

        panel.add(linkbox);
        JButton download = new JButton("Download");
        panel.add(download);
        frame.add(panel);
        frame.setBackground(Color.cyan);
        panel.setBackground(Color.DARK_GRAY);
        frame.setSize(400,400);
        frame.setVisible(true);
    }

}
