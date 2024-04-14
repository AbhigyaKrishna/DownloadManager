package me.groot.downloadmanager.services;

import java.net.URL;
import java.nio.file.Path;

public abstract class Downloader {

    protected final URL url;
    protected final Path downloadPath;
    protected final Progress progress = new Progress();

    public Downloader(URL url, Path downloadPath) {
        this.url = url;
        this.downloadPath = downloadPath;
    }

    public abstract void download();

    public URL getUrl() {
        return url;
    }  //constructor that tells about the url getting downloaded

    public Path getDownloadPath() {
        return downloadPath;
    } //constructor that tells about the path getting downloaded

    public Progress getProgress() {
        return progress;
    }

}
