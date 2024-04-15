package me.groot.downloadmanager.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondPage extends Screen {
    private JPanel panel;
    private JTextField linkbox;
    private JLabel transferRateLabel, timeLeftLabel, filesizeLabel, statusLabel;

    public SecondPage() {
        super("FILE NAME");
    }

    @Override
    public void initialize() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 3, 3));

        linkbox = new JTextField(300);
        panel.add(linkbox);

        transferRateLabel = new JLabel("Transfer Rate: ");
        panel.add(transferRateLabel);

        timeLeftLabel = new JLabel("Time Left: ");
        panel.add(timeLeftLabel);

        filesizeLabel = new JLabel("File Size: ");
        panel.add(filesizeLabel);

        statusLabel = new JLabel("Status: ");
        panel.add(statusLabel);

        JButton pauseButton = new JButton("<html><font size='4'>Pause</font></html>");
        panel.add(pauseButton);

        JButton resumeButton = new JButton("<html><font size='4'>Resume</font></html>");
        panel.add(resumeButton);

        JButton progressCancelButton = new JButton("<html><font size='4'>Cancel</font></html>");
        panel.add(progressCancelButton);

        // Create and add progress bar
        JProgressBar jb = new JProgressBar(0, 200);
        jb.setBounds(80, 80, 320, 60);
        jb.setValue(0);
        jb.setStringPainted(true);
        panel.add(jb);

        // Add action listeners to buttons
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your pause button action logic here
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your resume button action logic here
            }
        });

        progressCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your cancel button action logic here
            }
        });

        add(panel);

        setBackground(new Color(170, 132, 220));
        panel.setBackground(new Color(170, 132, 220));
        setSize(400, 300);
    }
}
