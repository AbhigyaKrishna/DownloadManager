package me.groot.downloadmanager.services;

import java.net.URL;
import java.nio.file.Path;

public abstract class ResumableDownloader extends Downloader {

    public ResumableDownloader(URL url, Path downloadPath) {
        super(url, downloadPath);
    }

    public abstract void pause();

    public abstract void resume();

}
