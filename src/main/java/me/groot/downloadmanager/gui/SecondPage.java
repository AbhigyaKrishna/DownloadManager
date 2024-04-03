package me.groot.downloadmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondPage {
    public JFrame frame;
    public JPanel panel;
    public JTextField linkbox;
    public JLabel transferRateLabel, timeLeftLabel, filesizeLabel, statusLabel;

    public SecondPage() {
        frame = new JFrame("DOWNLOADING PAGE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 5, 5));

        linkbox = new JTextField(32);
        panel.add(linkbox);

        transferRateLabel = new JLabel("Transfer Rate: ");
        panel.add(transferRateLabel);

        timeLeftLabel = new JLabel("Time Left: ");
        panel.add(timeLeftLabel);

        filesizeLabel = new JLabel("File Size: ");
        panel.add(filesizeLabel);

        statusLabel = new JLabel("Status: ");
        panel.add(statusLabel);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(pauseButton);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(resumeButton);

        JButton progressResumeButton = new JButton("Progress Resume");
        progressResumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(progressResumeButton);

        frame.add(panel);


        frame.setBackground(Color.cyan);
        panel.setBackground(Color.DARK_GRAY);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

}