package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.DifferenceProgress;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class SimpleDownloader extends Downloader<DifferenceProgress> {

    public SimpleDownloader(URL url, Path downloadPath) {
        super(url, downloadPath, new DifferenceProgress());
    }

    @Override
    public void initialize() {
        DownloadInfo info = getInfo();
        progress.setMax(info.length());
    }

    @Override
    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fos = new FileOutputStream(downloadPath.toFile())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
                progress.inc(bytesRead);
            }
        } catch (IOException e) {
            progress.handleError(e);
            throw new RuntimeException("Failed to download file", e);
        }
        progress.disposeAll();
        System.out.println("Downloaded " + url + " to " + downloadPath);
    }

}
