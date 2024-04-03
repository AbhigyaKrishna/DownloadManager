package me.groot.downloadmanager.services;

import java.net.URL;
import java.nio.file.Path;

public class SimpleResumableDownloader extends ResumableDownloader {

    public SimpleResumableDownloader(URL url, Path downloadPath) {
        super(url, downloadPath);
    }

    @Override
    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}
