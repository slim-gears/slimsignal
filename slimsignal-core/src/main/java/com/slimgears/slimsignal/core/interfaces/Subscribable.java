package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/7/2017.
 */
public interface Subscribable<S> {
    void subscribe(S subscriber);
    void unsubscribe(S subscriber);
}
