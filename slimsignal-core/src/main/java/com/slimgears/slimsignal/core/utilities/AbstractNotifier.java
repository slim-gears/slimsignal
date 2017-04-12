package com.slimgears.slimsignal.core.utilities;

import com.slimgears.slimsignal.core.interfaces.Subscription;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public abstract class AbstractNotifier<S> implements Notifier<S> {
    private final RecursionGuard recursionGuard = new RecursionGuard();
    protected abstract Stream<S> subscribers();

    protected Subscription register(Subscription subscription) {
        Subscriptions.current().add(subscription);
        return subscription;
    }

    @Override
    public void publish(Consumer<S> notification) {
        try (AutoCloseable ignored = recursionGuard.lock()) {
            subscribers().forEach(notification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isPublishing() {
        return recursionGuard.isAcquired();
    }
}
