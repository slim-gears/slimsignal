package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/7/2017.
 */
public interface Trigger extends Subscribable<Runnable> {
    void trigger();
}
