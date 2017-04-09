package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.utilities.Notifier;
import com.slimgears.slimsignal.core.utilities.WeakNotifier;

/**
 * Created by denis on 4/7/2017.
 */
public class InternalTrigger implements Trigger {
    private final Notifier<Runnable> notifier = new WeakNotifier<>();

    @Override
    public void subscribe(Runnable subscriber) {
        notifier.subscribe(subscriber);
    }

    @Override
    public void unsubscribe(Runnable subscriber) {
        notifier.unsubscribe(subscriber);
    }

    @Override
    public void trigger() {
        notifier.publish(Runnable::run);
    }
}
