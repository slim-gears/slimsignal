package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/9/2017.
 */
public interface Signal<T> extends Observable<T> {
    void publish(T value);
    Signal<T> distinct();
}
