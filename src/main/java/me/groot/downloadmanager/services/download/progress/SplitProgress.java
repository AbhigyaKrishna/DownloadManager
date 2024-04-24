package me.groot.downloadmanager.services.download.progress;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SplitProgress implements IProgress {

    private final Set<IProgress> internals = ConcurrentHashMap.newKeySet();
    private final List<Object> completed = new ArrayList<>();
    private Sinks.Many<Double> sink;
    private Flux<Double> flux;

    public void addProgress(IProgress progress) {
        internals.add(progress);
        if (progress.isComplete()) {
            completed.add(new Object());
            return;
        }
        progress.flux().subscribe(new InternalSubscriber());
    }

    @Override
    public double getProgress() {
        return internals.stream().mapToDouble(IProgress::getProgress).sum() / internals.size();
    }

    @Override
    public void setProgress(double progress) {
        throw new UnsupportedOperationException("Cannot set progress on split progress");
    }

    @Override
    public void inc(double increment) {
        throw new UnsupportedOperationException("Cannot increment progress on split progress");
    }

    @Override
    public void handleError(Throwable e) {
        if (sink != null) sink.tryEmitError(e);
        internals.forEach(progress -> progress.handleError(e));
    }

    @Override
    public Flux<Double> flux() {
        if (flux == null) {
            sink = Sinks.many().multicast().onBackpressureBuffer();
            flux = sink.asFlux();
        }
        return flux;
    }

    private void checkComplete() {
        System.out.println(completed.size() + " - " + internals.size());
        if (sink != null && completed.size() == internals.size()) {
            sink.tryEmitComplete();
        }
    }

    final class InternalSubscriber implements CoreSubscriber<Double> {
        @Override
        public void onSubscribe(Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(Double o) {
            if (flux != null) {
                sink.tryEmitNext(getProgress());
            }
        }

        @Override
        public void onError(Throwable throwable) {
        }

        @Override
        public void onComplete() {
            completed.add(new Object());
            checkComplete();
        }
    }

}
