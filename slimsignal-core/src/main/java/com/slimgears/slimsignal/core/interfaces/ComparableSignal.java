package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/7/2017.
 */
public interface ComparableSignal<T extends Comparable<T>> extends Signal<T> {
    BoolSignal lessThan(Signal<T> other);
    BoolSignal greaterThan(Signal<T> other);
    BoolSignal lessOrEqual(Signal<T> other);
    BoolSignal greaterOrEqual(Signal<T> other);
}
