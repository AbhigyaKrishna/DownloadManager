package me.groot.downloadmanager.services;

import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Downloader {

    protected final URL url;
    protected final Path downloadPath;
    protected final AtomicReference<Double> progress = new AtomicReference<>(0.0); // 0.0-1.0

    public Downloader(URL url, Path downloadPath) {
        this.url = url;
        this.downloadPath = downloadPath;
    }

    public abstract void download();

    public URL getUrl() {
        return url;
    }

    public Path getDownloadPath() {
        return downloadPath;
    }

    public double getProgress() {
        return progress.get();
    }

}
