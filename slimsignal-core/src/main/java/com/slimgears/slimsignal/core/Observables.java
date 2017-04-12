package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Observable;
import com.slimgears.slimsignal.core.interfaces.ObservableCollection;
import com.slimgears.slimsignal.core.interfaces.Signal;
import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.internal.AbstractSignal;
import com.slimgears.slimsignal.core.internal.DefaultObservableCollection;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class Observables {
    public static <T> Observable<T> create() {
        return Signals.create();
    }

    public static <T> Observable<T> create(Consumer<T> subscriber) {
        Observable<T> observable = Signals.create();
        observable.subscribe(subscriber);
        return observable;
    }

    public static Trigger triggerForAny(Observable... observables) {
        Trigger trigger = Triggers.create();
        Consumer subscriber = obj -> trigger.trigger();
        //noinspection unchecked
        Stream.of(observables).forEach(signal -> signal.subscribe(subscriber));
        return trigger;
    }

    public static <T> ObservableCollection<T> collection() {
        return new DefaultObservableCollection<>();
    }

    public static <T> Observable<T> of(T value) {
        return new AbstractSignal<T>() {
            @Override
            protected <R> Signal<R> createSignal() {
                return Signals.create();
            }

            @Override
            public T get() {
                return value;
            }
        };
    }

    private static <T1, T2, R> Observable<R> apply(Observable<T1> arg1, Observable<T2> arg2, BiFunction<T1, T2, R> function) {
        Trigger trigger = triggerForAny(arg1, arg2);
        Signal<T1> arg1Signal = Signals.toDistinctSignal(arg1);
        Signal<T2> arg2Signal = Signals.toDistinctSignal(arg2);
        Signal<R> resultSignal = Signals.createDistinct();
        Runnable listener = () -> resultSignal.publish(function.apply(arg1Signal.get(), arg2Signal.get()));
        trigger.subscribe(listener);
        listener.run();
        return resultSignal;
    }

    private static <T1, T2, R> Observable<R> apply(Observable<T1> arg1, T2 arg2, BiFunction<T1, T2, R> function) {
        return Observables.apply(arg1, Observables.of(arg2), function);
    }

    @SafeVarargs
    public static Observable<Boolean> and(Observable<Boolean>... args) {
        return collect(stream -> stream.filter(Objects::nonNull).allMatch(Boolean::booleanValue), args);
    }

    @SafeVarargs
    public static Observable<Boolean> or(Observable<Boolean>... args) {
        return collect(stream -> stream.filter(Objects::nonNull).allMatch(Boolean::booleanValue), args);
    }

    public static Observable<Boolean> not(Observable<Boolean> arg) {
        return arg.filter(Objects::nonNull).map(val -> !val);
    }

    @SafeVarargs
    public static <T, A, R> Observable<R> collect(Collector<? super T, A, R> collector, Observable<T>... observables) {
        return collect((Stream<T> stream) -> stream.collect(collector), observables);
    }

    @SafeVarargs
    public static <T, R> Observable<R> collect(Function<Stream<T>, R> collector, Predicate<T> predicate, Observable<T>... observables) {
        return collect(collector, toFiltered(predicate, observables));
    }

    @SafeVarargs
    public static <T, R> Observable<R> collect(Function<Stream<T>, R> collector, Observable<T>... observables) {
        Signal<T>[] argSignals = Signals.toDistinctSignals(observables);
        Trigger trigger = Observables.triggerForAny(argSignals);
        Signal<R> resultSignal = Signals.createDistinct();
        Runnable listener = () -> resultSignal.publish(collector.apply(Stream.of(argSignals).map(Signal::get)));

        if (Stream.of(observables).map(Observable::get).anyMatch(Objects::nonNull)) {
            listener.run();
        }

        trigger.subscribe(listener);
        return resultSignal;
    }

    public static <T> Observable<Boolean> areEqual(Observable<T> arg1, Observable<T> arg2) {
        return apply(arg1, arg2, (BiFunction<T, T, Boolean>) Objects::equals);
    }

    public static <T> Observable<Boolean> areEqual(Observable<T> arg1, T arg2) {
        return apply(arg1, arg2, Objects::equals);
    }

    public static <T extends Comparable<T>> Observable<Boolean> lessThan(Observable<T> arg1, Observable<T> arg2) {
        return Observables.<T, T, Boolean>apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) < 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> lessThan(Observable<T> arg1, T arg2) {
        return apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) < 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> greaterThan(Observable<T> arg1, Observable<T> arg2) {
        return Observables.<T, T, Boolean>apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) > 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> greaterThan(Observable<T> arg1, T arg2) {
        return apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) > 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> lessOrEqual(Observable<T> arg1, Observable<T> arg2) {
        return Observables.<T, T, Boolean>apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) <= 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> lessOrEqual(Observable<T> arg1, T arg2) {
        return apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) <= 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> greateOrEqual(Observable<T> arg1, Observable<T> arg2) {
        return Observables.<T, T, Boolean>apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) >= 0);
    }

    public static <T extends Comparable<T>> Observable<Boolean> greateOrEqual(Observable<T> arg1, T arg2) {
        return apply(arg1, arg2, (a1, a2) -> Comparator.<T>naturalOrder().compare(a1, a2) >= 0);
    }

    public static Observable<Boolean> contains(Observable<String> str, Observable<String> substr) {
        return apply(str, substr, String::contains);
    }

    public static Observable<Boolean> contains(Observable<String> str, String substr) {
        return apply(str, substr, String::contains);
    }

    public static Observable<Boolean> startsWith(Observable<String> str, Observable<String> prefix) {
        return Observables.<String, String, Boolean>apply(str, prefix, String::startsWith);
    }

    public static Observable<Boolean> startsWith(Observable<String> str, String prefix) {
        return apply(str, prefix, String::startsWith);
    }

    public static Observable<Boolean> endsWith(Observable<String> str, Observable<String> suffix) {
        return apply(str, suffix, String::endsWith);
    }

    public static Observable<Boolean> endsWith(Observable<String> str, String suffix) {
        return apply(str, suffix, String::endsWith);
    }

    @SafeVarargs
    public static <T> Observable<T>[] toFiltered(Predicate<T> predicate, Observable<T>... observables) {
        //noinspection unchecked
        return Stream.of(observables).map(observable -> observable.filter(predicate)).toArray(Observable[]::new);
    }
}
