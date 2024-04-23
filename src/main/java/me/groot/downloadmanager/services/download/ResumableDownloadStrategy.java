package me.groot.downloadmanager.services.download;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

public class ResumableDownloadStrategy extends SimpleResumableDownloadStrategy {

    public ResumableDownloadStrategy(URL url, Path downloadPath) {
        super(url, downloadPath);
    }

    @Override
    public void initialize() throws IOException {
        DownloadInfo info = getInfo();
        progress.setMax(info.length());

        File file = downloadPath.toFile();
        if (file.exists()) {
            progress.setProgress(file.length());
        } else {
            createFile();
        }

        if (progress.isComplete())
            throw new IllegalStateException("Download already completed");
    }

    @Override
    protected URLConnection openConnection() throws IOException {
        URLConnection connection = super.openConnection();
        connection.setRequestProperty("Range", "bytes=" + progress.getCurrent() + "-" + progress.getMax());
        return connection;
    }

}
