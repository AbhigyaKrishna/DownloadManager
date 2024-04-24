package me.groot.downloadmanager.gui;

import me.groot.downloadmanager.database.Database;
import me.groot.downloadmanager.jooq.codegen.Tables;
import me.groot.downloadmanager.services.download.DownloadStrategy;
import me.groot.downloadmanager.services.download.SplitDownloadStrategy;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class SecondPage extends Screen {
    private final String url;
    private final String filename;
    private final SplitDownloadStrategy strategy;
    private final Database db;
    private final Thread thread;

    public SecondPage(String url, String filename, Database db) {
        super("Downloading...");
        this.filename = filename;
        this.url = url;
        this.db = db;
        try {
            this.strategy = new SplitDownloadStrategy(new URI(url).toURL(), Path.of(filename));
            strategy.initialize();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.thread = new Thread(this.strategy::download);
    }

    @Override
    public void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create labels panel
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(0, 1));
        labelsPanel.setBackground(new Color(216, 209, 220));
        panel.add(labelsPanel);

        // Add labels
        JLabel linkbox = new JLabel("Link: " + url);
        linkbox.setSize(400, 25);
        labelsPanel.add(linkbox);
        DownloadStrategy.DownloadInfo info = strategy.getInfo();

        JLabel transferRateLabel = new JLabel("Transfer Rate: ");
        labelsPanel.add(transferRateLabel);

        JLabel timeLeftLabel = new JLabel("Time Left: ");
        labelsPanel.add(timeLeftLabel);

        JLabel filesizeLabel = new JLabel("File Size: " + ((double) info.length() / 1e+6) + " MB");
        labelsPanel.add(filesizeLabel);

        JLabel statusLabel = new JLabel("Status: Downloading");
        labelsPanel.add(statusLabel);

        // Create buttons panel with FlowLayout

        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));// Adjust alignment and spacing
        buttonsPanel.setBackground(new Color(216, 209, 220));

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


        JProgressBar jb = new JProgressBar(0, 100);
        jb.setBounds(80, 80, 320, 60);
        jb.setValue(1);
        jb.setStringPainted(true);
        panel.add(jb);
        strategy.getProgress().flux().onBackpressureLatest().subscribe(new Subscribers(jb, this, statusLabel));

        pauseButton.addActionListener(e -> {
            if (strategy.getProgress().isComplete())
                return;
            strategy.pause();
            statusLabel.setText("Status: Paused");
            // Add your pause button action logic here
        });

        resumeButton.addActionListener(e -> {
            if (strategy.getProgress().isComplete())
                return;
            strategy.resume();
            statusLabel.setText("Status: Downloading");
            //Add your resume button action logic here
        });

        progressCancelButton.addActionListener(e -> {
            strategy.pause();
            thread.interrupt();
            try {
                Files.delete(Path.of(filename));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Add your cancel button action logic here
        });

        add(panel);

        setBackground(new Color(170, 132, 220));
        panel.setBackground(new Color(170, 132, 220));

        setSize(400, 300);
        setLocationRelativeTo(null);
        thread.start();
    }

    final class Subscribers implements Subscriber<Double> {

        private final JProgressBar jb;
        private Subscription s;
        private final JFrame parentcomponent;
        private final JLabel status;

        public Subscribers(JProgressBar jb, JFrame parentcomponent, JLabel status) {
            this.parentcomponent = parentcomponent;
            this.jb = jb;
            this.status = status;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1);
            this.s = s;
        }

        @Override
        public void onNext(Double aDouble) {
            jb.setValue((int) (aDouble * 100));
            s.request(1);
        }

        @Override
        public void onError(Throwable t) {
            JOptionPane.showMessageDialog(parentcomponent, t.getMessage());
        }

        @Override
        public void onComplete() {
            jb.setValue(100);
            status.setText("Status: Completed");
            Mono.from(db.getContext().insertInto(Tables.HISTORY)
                    .set(Tables.HISTORY.FILE_NAME, filename)
                    .set(Tables.HISTORY.FILE_URL, url)
                    .set(Tables.HISTORY.FILE_LOCATION, new File(filename).getAbsolutePath())
                    .set(Tables.HISTORY.FILE_SIZE, strategy.getInfo().length())
                    .set(Tables.HISTORY.FILE_STATUS, "Done")
                    .set(Tables.HISTORY.FILE_DATETIME, LocalDateTime.now())
            ).subscribe();
        }
    }
}
