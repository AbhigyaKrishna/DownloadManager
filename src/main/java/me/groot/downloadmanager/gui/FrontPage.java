package me.groot.downloadmanager.gui;

import me.groot.downloadmanager.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FrontPage extends Screen {
    private final Database db;
    public FrontPage(Database db){
        super("Download Manager");
        this.db = db;
    }
    @Override
    public void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Label prompting user to enter URL
        JLabel promptLabel = new JLabel("<html><font size='5'>Thee shouldst en'r the url</font></html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Spanning 3 columns
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        panel.add(promptLabel, gbc);

        JTextField linkbox = new JTextField(32);
        linkbox.setHorizontalAlignment(JTextField.CENTER);
        linkbox.setPreferredSize(new Dimension(300, 30)); // Set preferred size
        linkbox.setForeground(Color.GRAY); // Set text color to gray
        linkbox.setText("Enter URL"); // Initial blurred out text
        linkbox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (linkbox.getText().equals("Enter URL")) {
                    linkbox.setText("");
                    linkbox.setForeground(Color.BLACK); // Change text color when focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (linkbox.getText().isEmpty()) {
                    linkbox.setText("Enter URL");
                    linkbox.setForeground(Color.GRAY); // Change text color when focus lost
                }
            }
        });
        gbc.gridx = 1; // Center column
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth
        panel.add(linkbox, gbc);


        JTextField fileNameField = new JTextField(32);
        fileNameField.setHorizontalAlignment(JTextField.CENTER);
        fileNameField.setPreferredSize(new Dimension(300, 30)); // Set preferred size
        fileNameField.setForeground(Color.GRAY); // Set text color to gray
        fileNameField.setText("Enter File Name"); // Initial blurred out text
        fileNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fileNameField.getText().equals("Enter File Name")) {
                    fileNameField.setText("");
                    fileNameField.setForeground(Color.BLACK); // Change text color when focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fileNameField.getText().isEmpty()) {
                    fileNameField.setText("Enter File Name");
                    fileNameField.setForeground(Color.GRAY); // Change text color when focus lost
                }
            }
        });
        gbc.gridx = 1; // Center column
        gbc.gridy = 2;
        panel.add(fileNameField, gbc);

        JButton download = new JButton("<html><font size='4'>Download</font></html>");
        download.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        gbc.gridx = 2; // Right of the linkbox
        gbc.gridy = 3; // Below the linkbox
        panel.add(download, gbc);

        download.addActionListener(new DownloadButtonListener(linkbox,this, fileNameField, db));

        JButton history = new JButton("<html><font size='4'>History</font></html>");
        history.setPreferredSize(new Dimension(120, 40)); // Set preferred size
        gbc.gridx = 0; // Left of the linkbox
        panel.add(history, gbc);

        history.addActionListener(action -> {
            HistoryPage hp = new HistoryPage(db.getContext());
            hp.initialize();
            hp.setVisible(true);
        });



        add(panel);
        setBackground(new Color(170,132,220));
        panel.setBackground(new Color(170,132,220));
        pack(); // Adjusts the frame size to fit the components
        setLocationRelativeTo(null); // Centers the frame on the screen

    }

    final static class DownloadButtonListener implements ActionListener {

        private final JTextField linkbox;
        private final JTextField fileNameField;
        private final JFrame parentcomponent;
        private final Database db;

        public DownloadButtonListener(JTextField linkbox,JFrame parentcomponent,JTextField fileNameField, Database db) {
            this.linkbox = linkbox;
            this.parentcomponent = parentcomponent;
            this.fileNameField = fileNameField;
            this.db = db;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(linkbox.getText().isEmpty()){
                JOptionPane.showMessageDialog(parentcomponent,"Enter a valid URL");
                return;
            }
            if(fileNameField.getText().isEmpty()){
                JOptionPane.showMessageDialog(parentcomponent,"Enter a valid File Name ");
                return;
            }

            SecondPage sp = new SecondPage(linkbox.getText(),fileNameField.getText(), db);
            sp.initialize();
            sp.setVisible(true);
        }
    }
}
