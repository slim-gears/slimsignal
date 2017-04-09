package com.slimgears.slimsignal.core.interfaces;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by denis on 4/7/2017.
 */
public interface CollectionSignal<T> extends Supplier<Collection<T>> {
    Iterable<Signal<T>> items();
    IntSignal size();
    Signal<T> added();
    Signal<T> removed();
    Signal<T> item(T item);
    BoolSignal contains(Signal<T> item);
    BoolSignal isEmpty();

    Trigger asTrigger();
    CollectionSignal<T> filter(Predicate<T> predicate);
    <R> CollectionSignal<R> map(Function<T, R> mapper);
    <R, A> Signal<R> collect(Collector<? super T, A, R> collector);

    default void add(T value) {
        added().publish(value);
    }

    default void addAll(Collection<T> values) {
        values.forEach(this::add);
    }

    default void remove(T value) {
        removed().publish(value);
    }

    default void removeAll(Collection<T> values) {
        values.forEach(this::remove);
    }

    default void removeIf(Predicate<T> predicate) {
        get().stream().filter(predicate.negate()).forEach(this::remove);
    }

    void clear();
}
