package me.groot.downloadmanager.services;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.HashSet;
import java.util.Set;

public class Progress extends Observable<Double> {

    private final Set<ProgressWatcher> watchers = new HashSet<>();
    private double progress = 0.0; // 0.0-1.0

    @Override
    protected void subscribeActual(@NonNull Observer<? super Double> observer) {
        final ProgressWatcher watcher = new ProgressWatcher(observer);
        watchers.add(watcher);
        observer.onSubscribe(watcher);
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        watchers.forEach(ProgressWatcher::update);
    }

    public void inc(double increment) {
        setProgress(progress + increment);
    }

    final class ProgressWatcher implements Disposable {
        private final Observer<? super Double> observer;

        ProgressWatcher(Observer<? super Double> observer) {
            this.observer = observer;
        }

        public void update() {
            if (isDisposed()) return;
            if (progress >= 1.0) {
                observer.onComplete();
                return;
            }

            observer.onNext(progress);
        }

        @Override
        public void dispose() {
            watchers.remove(this);
        }

        @Override
        public boolean isDisposed() {
            return !watchers.contains(this);
        }
    }

}
