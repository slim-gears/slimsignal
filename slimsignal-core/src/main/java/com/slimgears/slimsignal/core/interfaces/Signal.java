package com.slimgears.slimsignal.core.interfaces;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by denis on 4/7/2017.
 */
public interface Signal<T> extends Subscribable<Consumer<T>>, Supplier<T> {
    void publish(T value);
    BoolSignal equalsTo(Signal<T> other);
    Trigger asTrigger();

    <S extends Signal<T>> S filter(Predicate<T> predicate);
    <S extends Signal<T>> S distinct();
    <R, S extends Signal<R>> S map(Function<T, R> mapper);

    BoolSignal mapToBool(Function<T, Boolean> mapper);
    IntSignal mapToInt(Function<T, Integer> mapper);
    LongSignal mapToLong(Function<T, Long> mapper);
    DoubleSignal mapToDouble(Function<T, Double> mapper);
    FloatSignal mapToFloat(Function<T, Float> mapper);
    StringSignal mapToString(Function<T, String> mapper);
}
