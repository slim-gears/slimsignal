package com.slimgears.slimsignal.core.utilities;

import com.slimgears.slimsignal.core.interfaces.Subscribable;

import java.util.function.Consumer;

/**
 * Created by denis on 4/7/2017.
 */
public interface Notifier<S> extends Subscribable<S> {
    void publish(Consumer<S> notification);
    boolean isPublishing();
}
