package com.slimgears.slimsignal.core.utilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by itskod on 06/04/2017.
 */
public abstract class Subscriptions {
    private final static ThreadLocal<SubscriptionStore> CURRENT_THREAD_SUBSCRIPTIONS = new ThreadLocal<>();

    interface SubscriptionStore extends AutoCloseable {
        <S> void add(S subscription, Consumer<S> unsubscriber);
    }

    public static SubscriptionStore current() {
        return Optional
                .ofNullable(CURRENT_THREAD_SUBSCRIPTIONS.get())
                .orElseGet(() -> {
                    SubscriptionStore instance = new InternalSubscriber();
                    CURRENT_THREAD_SUBSCRIPTIONS.set(instance);
                    return instance;
                });
    }

    private static SubscriptionStore setCurrent(SubscriptionStore store) {
        SubscriptionStore prev = current();
        CURRENT_THREAD_SUBSCRIPTIONS.set(store);
        return prev;
    }

    public void subscribe(Runnable runnable) {
        try (AutoCloseable ignored = subscribe()) {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void unsubscribe();
    public abstract AutoCloseable subscribe();

    static class InternalSubscriber extends Subscriptions implements SubscriptionStore {
        private final List<Runnable> unsubscribers = new LinkedList<>();

        @Override
        public AutoCloseable subscribe() {
            SubscriptionStore prev = setCurrent(this);
            return () -> Subscriptions.setCurrent(prev);
        }

        @Override
        public void unsubscribe() {
            unsubscribers.forEach(Runnable::run);
        }

        @Override
        public <S> void add(S subscription, Consumer<S> unsubscriber) {
            unsubscribers.add(() -> unsubscriber.accept(subscription));
        }

        @Override
        public void close() throws Exception {
            unsubscribe();
        }
    }

    public static Subscriptions create() {
        return new InternalSubscriber();
    }
}
