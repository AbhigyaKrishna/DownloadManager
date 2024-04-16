package me.groot.downloadmanager.gui;

import javax.swing.*;
import java.awt.*;

public class SecondPage extends Screen {

    public SecondPage() {
        super("DOWNLOAD MANAGER");
    }

    @Override
    public void initialize() {


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

// Create labels panel
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(0, 1));
        panel.add(labelsPanel);

// Add labels
        JLabel linkbox = new JLabel("LINK:");
        linkbox.setSize(400, 25);
        labelsPanel.add(linkbox);

        JLabel transferRateLabel = new JLabel("Transfer Rate: ");
        labelsPanel.add(transferRateLabel);

        JLabel timeLeftLabel = new JLabel("Time Left: ");
        labelsPanel.add(timeLeftLabel);

        JLabel filesizeLabel = new JLabel("File Size: ");
        labelsPanel.add(filesizeLabel);

        JLabel statusLabel = new JLabel("Status: ");
        labelsPanel.add(statusLabel);

// Create buttons panel with FlowLayout

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));  // Adjust alignment and spacing
        panel.add(buttonsPanel);

// Create buttons with smaller preferred size and check minimum size
        JButton pauseButton = new JButton("<html><font size='2'>Pause</font></html>");
        pauseButton.setPreferredSize(new Dimension(80, 30));
        Dimension minSize = pauseButton.getMinimumSize();
        if (minSize.width > 80 || minSize.height > 30) {
            pauseButton.setMinimumSize(new Dimension(80, 30));  // Set minimum size
        }
        buttonsPanel.add(pauseButton);

        JButton resumeButton = new JButton("<html><font size='2'>Resume</font></html>");
        resumeButton.setPreferredSize(new Dimension(80, 30));
        minSize = resumeButton.getMinimumSize();
        if (minSize.width > 80 || minSize.height > 30) {
            resumeButton.setMinimumSize(new Dimension(80, 30));
        }
        buttonsPanel.add(resumeButton);

        JButton progressCancelButton = new JButton("<html><font size='2'>Cancel</font></html>");
        progressCancelButton.setPreferredSize(new Dimension(80, 30));
        minSize = progressCancelButton.getMinimumSize();
        if (minSize.width > 80 || minSize.height > 30) {
            progressCancelButton.setMinimumSize(new Dimension(80, 30));
        }
        buttonsPanel.add(progressCancelButton);


        JProgressBar jb = new JProgressBar(0, 200);
        jb.setBounds(80, 80, 320, 60);
        jb.setValue(1);
        jb.setStringPainted(true);
        panel.add(jb);


        pauseButton.addActionListener(e -> {
            // Add your pause button action logic here
        });

        resumeButton.addActionListener(e -> {
            // Add your resume button action logic here
        });

        progressCancelButton.addActionListener(e -> {
            // Add your cancel button action logic here
        });

        add(panel);


        setBackground(new Color(170, 132, 220));
        panel.setBackground(new Color(170, 132, 220));

        setSize(400, 300);
        setLocationRelativeTo(null);

    }
}
