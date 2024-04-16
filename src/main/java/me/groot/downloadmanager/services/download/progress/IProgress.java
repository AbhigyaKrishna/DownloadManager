package me.groot.downloadmanager.services.download.progress;

import reactor.core.publisher.Flux;

public interface IProgress {

    double getProgress();

    void setProgress(double progress);

    default boolean isComplete() {
        return getProgress() >= 1.0;
    }

    void inc(double increment);

    default void reset() {
        setProgress(0.0);
    }

    default void complete() {
        if (isComplete()) return;
        setProgress(1.0);
    }

    void handleError(Throwable e);

    Flux<Double> flux();

}
