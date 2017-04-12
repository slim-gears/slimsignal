package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Observable;
import com.slimgears.slimsignal.core.interfaces.Signal;
import com.slimgears.slimsignal.core.internal.DefaultSignal;
import com.slimgears.slimsignal.core.internal.DistinctSignal;

import java.util.stream.Stream;

/**
 * Created by denis on 4/9/2017.
 */
public class Signals {
    public static <T> Signal<T> create() {
        return new DefaultSignal<>();
    }

    public static <T> Signal<T> createDistinct() {
        return new DistinctSignal<>();
    }

    public static <T> Signal<T> toDistinctSignal(Observable<T> observable) {
        if (observable instanceof DistinctSignal) {
            return (DistinctSignal<T>)observable;
        }

        Signal<T> signal = createDistinct();
        observable.subscribe(signal::publish);
        return signal;
    }

    public static <T> Signal<T> toSignal(Observable<T> observable) {
        if (observable instanceof Signal) {
            return (Signal<T>)observable;
        } else {
            Signal<T> signal = create();
            observable.subscribe(signal::publish);
            return signal;
        }
    }

    @SafeVarargs
    public static <T> Signal<T>[] toSignals(Observable<T>... observables) {
        //noinspection unchecked
        return Stream.of(observables).map(Signals::toSignal).toArray(Signal[]::new);
    }

    @SafeVarargs
    public static <T> Signal<T>[] toDistinctSignals(Observable<T>... observables) {
        //noinspection unchecked
        return Stream.of(observables).map(Signals::toDistinctSignal).toArray(Signal[]::new);
    }
}
