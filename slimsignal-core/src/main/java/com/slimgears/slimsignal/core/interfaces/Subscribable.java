package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/7/2017.
 */
public interface Subscribable<S> {
    Subscription subscribe(S subscriber);
}
