package me.groot.downloadmanager.services.download;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleResumableDownloadStrategy extends SimpleDownloadStrategy implements Resumable {

    private final AtomicBoolean paused = new AtomicBoolean(false);

    public SimpleResumableDownloadStrategy(URL url, Path downloadPath) {
        super(url, downloadPath);
    }

    @Override
    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);
        try (BufferedInputStream in = new BufferedInputStream(openConnection().getInputStream());
             FileOutputStream fos = new FileOutputStream(downloadPath.toFile())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
                progress.inc(bytesRead);
                while (paused.get()) {
                    Thread.sleep(500);
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
