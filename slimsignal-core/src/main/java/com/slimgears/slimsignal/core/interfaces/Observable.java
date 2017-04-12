package com.slimgears.slimsignal.core.interfaces;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by denis on 4/7/2017.
 */
public interface Observable<T> extends Subscribable<Consumer<T>>, Supplier<T> {
    Trigger asTrigger();
    Observable<T> filter(Predicate<T> predicate);
    Observable<T> distinct();
    <R> Observable<R> map(Function<T, R> mapper);
}
