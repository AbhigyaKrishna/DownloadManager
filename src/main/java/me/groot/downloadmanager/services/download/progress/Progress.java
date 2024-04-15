package me.groot.downloadmanager.services.download.progress;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Progress extends AbstractProgress {

    private final Set<ProgressWatcher> watchers = ConcurrentHashMap.newKeySet();
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

    @Override
    public void handleError(Throwable e) {
        watchers.forEach(watcher -> watcher.observer.onError(e));
        watchers.clear();
    }

    @Override
    public void disposeAll() {
        new HashSet<>(watchers).forEach(ProgressWatcher::dispose);
    }

    final class ProgressWatcher implements Disposable {
        private final Observer<? super Double> observer;

        ProgressWatcher(Observer<? super Double> observer) {
            this.observer = observer;
        }

        public void update() {
            if (isDisposed()) return;
            if (isComplete()) {
                dispose();
                return;
            }

            observer.onNext(progress);
        }

        @Override
        public void dispose() {
            if (watchers.remove(this)) {
                observer.onComplete();
            }
        }

        @Override
        public boolean isDisposed() {
            return !watchers.contains(this);
        }
    }
}
