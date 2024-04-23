package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.IProgress;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

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

    public abstract void initialize() throws IOException;

    protected URLConnection openConnection() throws IOException {
        return url.openConnection();
    }

    public void download() {
        System.out.println("Downloading " + url + " to " + downloadPath);
        try (BufferedInputStream in = new BufferedInputStream(openConnection().getInputStream());
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
        progress.complete();
        System.out.println("Downloaded " + url + " to " + downloadPath);
    }

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
        return getInfo(false, Collections.emptyMap());
    }

    public DownloadInfo getInfo(boolean fetch, Map<String, String> headers) {
        if (info != null && !fetch) return info;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            headers.forEach(connection::setRequestProperty);
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

    protected final void createFile() throws IOException {
        downloadPath.toFile().createNewFile();
    }

    public record DownloadInfo(long length, String type) {
    }

}
