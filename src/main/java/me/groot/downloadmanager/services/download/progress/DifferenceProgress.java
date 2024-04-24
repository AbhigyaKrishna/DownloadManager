package me.groot.downloadmanager.services.download.progress;

import reactor.core.publisher.Flux;

public class DifferenceProgress implements IProgress {

    private long max;
    private long current;
    private final Progress internal;

    public DifferenceProgress(long max) {
        this.max = max;
        this.internal = new Progress();
    }

    public DifferenceProgress() {
        this(1);
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
        internal.setProgress((double) current / (double) max);
    }

    @Override
    public double getProgress() {
        return internal.getProgress();
    }

    @Override
    public void setProgress(double progress) {
        current = (long) (progress * max);
        internal.setProgress(progress);
    }

    @Override
    public void inc(double increment) {
        setCurrent(current + (long) increment);
    }

    @Override
    public void complete() {
        internal.complete();
    }

    @Override
    public void handleError(Throwable e) {
        internal.handleError(e);
    }

    @Override
    public Flux<Double> flux() {
        return internal.flux();
    }
}
