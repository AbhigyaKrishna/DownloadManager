package me.groot.downloadmanager.services.download.progress;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class Progress implements IProgress {

    private double progress = 0.0; // 0.0-1.0
    private Sinks.Many<Double> sink;
    private Flux<Double> flux;

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        if (flux != null) {
            sink.tryEmitNext(progress);
        }
        this.progress = progress;
    }

    public void inc(double increment) {
        setProgress(progress + increment);
    }

    @Override
    public void handleError(Throwable e) {
        if (flux != null) {
            sink.tryEmitError(e);
        }
    }

    @Override
    public Flux<Double> flux() {
        if (flux == null) {
            sink = Sinks.many().multicast().onBackpressureBuffer();
            flux = sink.asFlux();
        }
        return flux;
    }
}
