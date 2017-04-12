package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.Signals;
import com.slimgears.slimsignal.core.interfaces.Observable;
import com.slimgears.slimsignal.core.interfaces.Signal;
import com.slimgears.slimsignal.core.interfaces.Subscription;
import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.utilities.Consumers;
import com.slimgears.slimsignal.core.utilities.Lazy;
import com.slimgears.slimsignal.core.utilities.Notifier;
import com.slimgears.slimsignal.core.utilities.WeakNotifier;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by denis on 4/9/2017.
 */
public abstract class AbstractSignal<T> implements Signal<T> {
    protected final Supplier<Signal<T>> distinctSignal;
    protected final Notifier<Consumer<T>> notifier = new WeakNotifier<>();

    protected AbstractSignal() {
        distinctSignal = Lazy.of(() -> Signals.toDistinctSignal(this));
    }

    public Subscription subscribe(Consumer<T> subscriber) {
        return notifier.subscribe(subscriber);
    }

    public Trigger asTrigger() {
        return new Trigger() {
            @Override
            public void trigger() {

            }

            @Override
            public Subscription subscribe(Runnable subscriber) {
                return AbstractSignal.this.subscribe(val -> subscriber.run());
            }
        };
    }

    public Observable<T> filter(Predicate<T> predicate) {
        Signal<T> signal = createSignal();
        this.subscribe(Consumers.filter(signal::publish, predicate));
        return signal;
    }

    @Override
    public Signal<T> distinct() {
        return distinctSignal.get();
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        Signal<R> signal = createSignal();
        subscribe(val -> signal.publish(mapper.apply(val)));
        return signal;
    }

    protected abstract <R> Signal<R> createSignal();

    public synchronized void publish(T value) {
        if (!isPublishing()) {
            notifier.publish(s -> s.accept(value));
        }
    }

    protected boolean isPublishing() {
        return notifier.isPublishing();
    }
}
