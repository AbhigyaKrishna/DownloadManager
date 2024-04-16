package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.IProgress;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public abstract class DownloadStrategy<P extends IProgress> {

    protected final URL url;
    protected final Path downloadPath;
    protected final P progress;
    private DownloadInfo info;

    public DownloadStrategy(URL url, Path downloadPath, P progress) {
        this.url = url;
        this.downloadPath = downloadPath;
        this.progress = progress;
    }

    public abstract void initialize();

    public abstract void download();

    public URL getUrl() {
        return url;
    }

    public Path getDownloadPath() {
        return downloadPath;
    }

    public P getProgress() {
        return progress;
    }

    public boolean isResumable() {
        return this instanceof Resumable;
    }

    public DownloadInfo getInfo() {
        return getInfo(false);
    }

    public DownloadInfo getInfo(boolean fetch) {
        if (info != null && !fetch) return info;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            final long length = connection.getContentLengthLong();
            final String type = connection.getContentType();
            connection.disconnect();
            DownloadInfo info = new DownloadInfo(length, type);
            this.info = info;
            return info;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get file info", e);
        }
    }

    public record DownloadInfo(long length, String type) {
    }

}
