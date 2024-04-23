package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.DifferenceProgress;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class SimpleDownloadStrategy extends DownloadStrategy<DifferenceProgress> {

    public SimpleDownloadStrategy(URL url, Path downloadPath) {
        super(url, downloadPath, new DifferenceProgress());
    }

    @Override
    public void initialize() throws IOException {
        DownloadInfo info = getInfo();
        progress.setMax(info.length());
        createFile();
    }

}
