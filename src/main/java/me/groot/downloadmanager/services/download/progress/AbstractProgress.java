package me.groot.downloadmanager.services.download.progress;

import io.reactivex.rxjava3.core.Observable;

public abstract class AbstractProgress extends Observable<Double> {

    public abstract double getProgress();

    public abstract void setProgress(double progress);

    public boolean isComplete() {
        return getProgress() >= 1.0;
    }

    public abstract void inc(double increment);

    public abstract void handleError(Throwable e);

    public abstract void disposeAll();

}
