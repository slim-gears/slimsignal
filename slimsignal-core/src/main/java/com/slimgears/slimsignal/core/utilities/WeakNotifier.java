package com.slimgears.slimsignal.core.utilities;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class WeakNotifier<S> extends AbstractNotifier<S> {
    private final Collection<S> subscribers = new WeakCollection<>();

    @Override
    public void subscribe(S subscriber) {
        subscribers.add(subscriber);
        register(subscriber);
    }

    @Override
    public void unsubscribe(S subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    protected Stream<S> subscribers() {
        return subscribers.stream();
    }
}
