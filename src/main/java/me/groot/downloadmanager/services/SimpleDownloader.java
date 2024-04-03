package me.groot.downloadmanager.services;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class SimpleDownloader extends Downloader {

    public SimpleDownloader(URL url, Path downloadPath) {
        super(url, downloadPath);
    }

    @Override
    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(downloadPath.toFile())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
        System.out.println("Downloaded " + url + " to " + downloadPath);
    }

}
