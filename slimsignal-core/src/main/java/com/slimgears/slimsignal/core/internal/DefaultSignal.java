package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.Subscription;

import java.util.function.Consumer;

/**
 * Created by denis on 4/7/2017.
 */
public class DefaultSignal<T> extends AbstractSignal<T> {
    private T lastValue;

    public DefaultSignal() {
        subscribe(value -> this.lastValue = value);
    }

    @Override
    public synchronized Subscription subscribe(Consumer<T> subscriber) {
        if (lastValue != null) {
            subscriber.accept(lastValue);
        }
        return super.subscribe(subscriber);
    }

    @Override
    protected <R> AbstractSignal<R> createSignal() {
        return new DefaultSignal<>();
    }

    @Override
    public T get() {
        return lastValue;
    }
}
