package com.slimgears.slimsignal.core.interfaces;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by denis on 4/7/2017.
 */
public interface ObservableCollection<T> extends Supplier<Collection<T>> {
    Iterable<Observable<T>> items();
    Observable<Integer> size();
    Observable<T> added();
    Observable<T> removed();
    Observable<T> item(T item);
    Observable<Boolean> contains(Observable<T> item);
    Observable<Boolean> isEmpty();

    Trigger asTrigger();
    ObservableCollection<T> filter(Predicate<T> predicate);
    <R> ObservableCollection<R> map(Function<T, R> mapper);
    <R> ObservableCollection<R> flatMap(Function<T, Iterable<R>> mapper);
    <R, A> Observable<R> collect(Collector<? super T, A, R> collector);

    void clear();
    void add(T item);
    void remove(T item);

    default void addAll(Iterable<T> items) {
        StreamSupport.stream(items.spliterator(), false).forEach(this::add);
    }

    default void removeAll(Iterable<T> items) {
        StreamSupport
                .stream(items.spliterator(), false)
                .collect(Collectors.toList())
                .forEach(this::remove);
    }
}
