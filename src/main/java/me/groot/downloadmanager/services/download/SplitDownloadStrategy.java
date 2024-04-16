package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.IProgress;
import me.groot.downloadmanager.services.download.progress.SplitProgress;

import java.net.URL;
import java.nio.file.Path;

public class SplitDownloadStrategy extends DownloadStrategy<SplitProgress> implements Resumable {

    public SplitDownloadStrategy(URL url, Path downloadPath) {
        super(url, downloadPath, new SplitProgress());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void download() {

    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
