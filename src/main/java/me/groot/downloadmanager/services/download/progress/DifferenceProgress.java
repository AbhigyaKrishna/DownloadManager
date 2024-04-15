package me.groot.downloadmanager.services.download.progress;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class DifferenceProgress extends AbstractProgress {

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

    @Override
    protected void subscribeActual(@NonNull Observer<? super Double> observer) {
        internal.subscribe(observer);
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
        internal.setProgress((double) current / max);
    }

    @Override
    public double getProgress() {
        return internal.getProgress();
    }

    @Override
    public void setProgress(double progress) {
        current = (long) progress * max;
        internal.setProgress(progress);
    }

    @Override
    public void inc(double increment) {
        setCurrent(current + (long) increment);
    }

    @Override
    public void handleError(Throwable e) {
        internal.handleError(e);
    }

    @Override
    public void disposeAll() {
        internal.disposeAll();
    }

}
