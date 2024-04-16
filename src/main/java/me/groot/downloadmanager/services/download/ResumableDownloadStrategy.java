package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.DifferenceProgress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResumableDownloadStrategy extends DownloadStrategy<DifferenceProgress> implements Resumable {

    private final AtomicBoolean paused = new AtomicBoolean(false);

    public ResumableDownloadStrategy(URL url, Path downloadPath) {
        super(url, downloadPath, new DifferenceProgress());
    }

    @Override
    public void initialize() {
        DownloadInfo info = getInfo();
        progress.setMax(info.length());

        File file = downloadPath.toFile();
        if (file.exists()) {
            progress.setProgress(file.length());
        }

        if (progress.isComplete())
            throw new RuntimeException("File already downloaded.");
    }

    @Override
    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);
        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + progress.getCurrent() + "-" + progress.getMax());
            try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream fos = new FileOutputStream(downloadPath.toFile())) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fos.write(dataBuffer, 0, bytesRead);
                    progress.inc(bytesRead);
                    while (isPaused()) {
                        Thread.sleep(500);
                    }
                }
            }
        } catch (IOException e) {
            progress.handleError(e);
            throw new RuntimeException("Failed to download file", e);
        } catch (InterruptedException e) {
            progress.handleError(e);
            throw new RuntimeException(e);
        }
        progress.complete();
        System.out.println("Downloaded " + url + " to " + downloadPath);
    }

    @Override
    public boolean isPaused() {
        return paused.get();
    }

    @Override
    public void pause() {
        paused.set(true);
    }

    @Override
    public void resume() {
        paused.set(false);
    }
}
