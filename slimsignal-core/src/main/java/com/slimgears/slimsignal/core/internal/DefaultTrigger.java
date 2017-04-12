package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.Subscription;
import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.utilities.Notifier;
import com.slimgears.slimsignal.core.utilities.WeakNotifier;

/**
 * Created by denis on 4/7/2017.
 */
public class DefaultTrigger implements Trigger {
    private final Notifier<Runnable> notifier = new WeakNotifier<>();

    @Override
    public Subscription subscribe(Runnable subscriber) {
        return notifier.subscribe(subscriber);
    }

    public void trigger() {
        notifier.publish(Runnable::run);
    }
}
