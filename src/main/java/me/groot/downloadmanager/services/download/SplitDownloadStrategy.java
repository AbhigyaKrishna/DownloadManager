package me.groot.downloadmanager.services.download;

import me.groot.downloadmanager.services.download.progress.SplitProgress;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplitDownloadStrategy extends DownloadStrategy<SplitProgress> implements Resumable {

    public static final int DEFAULT_PARTS = 8;

    private final int parts;
    private final Path partsDir;
    private final List<Split> internals;
    private final ExecutorService executor;

    public SplitDownloadStrategy(URL url, Path downloadPath) {
        this(url, downloadPath, DEFAULT_PARTS);
    }

    public SplitDownloadStrategy(URL url, Path downloadPath, int parts) {
        super(url, downloadPath, new SplitProgress());
        this.parts = parts;
        this.partsDir = downloadPath.getParent().resolve( "." + downloadPath.getFileName() + ".parts");
        this.internals = new ArrayList<>(parts);
        this.executor = Executors.newFixedThreadPool(parts);
    }

    @Override
    public void initialize() {
        DownloadInfo info = getInfo();
        File file = downloadPath.toFile();
        if (file.exists() && file.length() == info.length())
            throw new IllegalStateException("Download already completed");

        long contentParts = info.length() / parts;
        long currLen = 0;
        File dir = partsDir.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (int i = 0; i < parts; i++) {
            Path path = partsDir.resolve(i + ".part");
            Split split;
            if (i == parts - 1) {
                split = new Split(url, path, currLen, info.length());
            } else {
                split = new Split(url, path, currLen, currLen + contentParts);
            }
            internals.add(split);
            currLen += contentParts;

            try {
                split.initialize();
                progress.addProgress(split.getProgress());
            } catch (IllegalStateException ignored) {
            } catch (IOException e) {
                progress.handleError(e);
                throw new RuntimeException("Failed to initialize download", e);
            }
        }
    }

    @Override
    public void download() {
        CountDownLatch latch = new CountDownLatch(parts);
        for (Split split : internals) {
            executor.submit(() -> {
                while (true) {
                    try {
                        split.download();
                        break;
                    } catch (Exception e) {
                        try {
                            split.initialize();
                        } catch (IOException io) {
                            progress.handleError(io);
                            break;
                        }
                    }
                }
                latch.countDown();
            });
        }

        progress.flux().subscribe(__ -> {}, err -> {
            executor.shutdownNow();
            while (latch.getCount() > 0) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        executor.shutdown();
    }

    @Override
    public boolean isPaused() {
        return internals.getFirst().isPaused();
    }

    @Override
    public void pause() {
        internals.forEach(Split::pause);
    }

    @Override
    public void resume() {
        internals.forEach(Split::resume);
    }

    static final class Split extends SimpleResumableDownloadStrategy {

        private final long from;
        private final long to;

        public Split(URL url, Path downloadPath, long from, long to) {
            super(url, downloadPath);
            this.from = from;
            this.to = to;
        }

        @Override
        public void initialize() throws IOException {
            File file = downloadPath.toFile();
            progress.setMax(to - from);
            if (file.exists()) {
                progress.setCurrent(file.length());
            } else {
                createFile();
            }
            if (progress.isComplete())
                throw new IllegalStateException("Download already completed");
        }

        @Override
        protected URLConnection openConnection() throws IOException {
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + (from + progress.getCurrent()) + "-" + to);
            return connection;
        }
    }
}
