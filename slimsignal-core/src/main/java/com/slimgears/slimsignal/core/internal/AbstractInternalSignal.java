package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.Signals;
import com.slimgears.slimsignal.core.interfaces.*;
import com.slimgears.slimsignal.core.utilities.Consumers;
import com.slimgears.slimsignal.core.utilities.Lazy;
import com.slimgears.slimsignal.core.utilities.Notifier;
import com.slimgears.slimsignal.core.utilities.WeakNotifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * Created by denis on 4/7/2017.
 */
public abstract class AbstractInternalSignal<T, C extends AbstractInternalSignal<T, C>> implements Signal<T> {
    private final Supplier<C> distinctSignal;
    private final Supplier<C> clonner;
    private final Notifier<Consumer<T>> notifier = new WeakNotifier<>();
    private T lastValue;

    protected AbstractInternalSignal(Supplier<C> clonner) {
        distinctSignal = Lazy.of(this::createDistinctSignal);
        this.clonner = clonner;
    }

    @Override
    public void subscribe(Consumer<T> subscriber) {
        notifier.subscribe(subscriber);
        if (lastValue != null) {
            subscriber.accept(lastValue);
        }
    }

    @Override
    public void unsubscribe(Consumer<T> subscriber) {
        notifier.unsubscribe(subscriber);
    }

    @Override
    public void publish(T value) {
        if (!notifier.isPublishing()) {
            lastValue = value;
            notifier.publish(s -> s.accept(value));
        }
    }

    @Override
    public Trigger asTrigger() {
        return new Trigger() {
            private final Map<Runnable, Consumer<T>> subscribers = new HashMap<>();

            @Override
            public void subscribe(Runnable runnable) {
                Consumer<T> subscriber = val -> runnable.run();
                subscribers.put(runnable, subscriber);
                AbstractInternalSignal.this.subscribe(subscriber);
            }

            @Override
            public void unsubscribe(Runnable runnable) {
                Optional
                        .ofNullable(subscribers.get(runnable))
                        .ifPresent(AbstractInternalSignal.this::unsubscribe);
            }

            @Override
            public void trigger() {

            }
        };
    }

    @Override
    public <S extends Signal<T>> S filter(Predicate<T> predicate) {
        //noinspection unchecked
        S signal = (S)clone();
        this.subscribe(Consumers.filter(signal::publish, predicate));
        signal.subscribe(this::publish);
        return signal;
    }

    @Override
    public C distinct() {
        return distinctSignal.get();
    }

    @Override
    public BoolSignal equalsTo(Signal<T> other) {
        return forCondition(other, Objects::equals);
    }

    @Override
    public <R, S extends Signal<R>> S map(Function<T, R> mapper) {
        //noinspection unchecked
        return mapToSignal(() -> (S)clone(), mapper);
    }

    @Override
    public BoolSignal mapToBool(Function<T, Boolean> mapper) {
        return mapToSignal(InternalBoolSignal::new, mapper);
    }

    @Override
    public IntSignal mapToInt(Function<T, Integer> mapper) {
        return this.mapToSignal(InternalIntSignal::new, mapper);
    }

    @Override
    public LongSignal mapToLong(Function<T, Long> mapper) {
        return this.mapToSignal(InternalLongSignal::new, mapper);
    }

    @Override
    public DoubleSignal mapToDouble(Function<T, Double> mapper) {
        return this.mapToSignal(InternalDoubleSignal::new, mapper);
    }

    @Override
    public FloatSignal mapToFloat(Function<T, Float> mapper) {
        return this.mapToSignal(InternalFloatSignal::new, mapper);
    }

    @Override
    public StringSignal mapToString(Function<T, String> mapper) {
        return this.mapToSignal(InternalStringSignal::new, mapper);
    }

    protected <R> BoolSignal forCondition(Signal<R> argument, BiFunction<T, R, Boolean> condition) {
        BoolSignal signal = new InternalBoolSignal();
        Trigger trigger = Signals.triggerForAny(this, argument);
        trigger.subscribe(() ->
                signal.publish(condition.apply(get(), argument.get())));
        trigger.trigger();
        return signal;
    }

    protected <R, S extends Signal<R>> S mapToSignal(Supplier<S> signalSupplier, Function<T, R> mapper) {
        S signal = signalSupplier.get();
        subscribe(val -> signal.publish(mapper.apply(val)));
        return signal;
    }

    protected <R, S extends Signal<R>> S mapToSignal(Supplier<S> signalSupplier, Function<T, R> mapper, Function<R, T> backMapper) {
        S signal = mapToSignal(signalSupplier, mapper);
        signal.subscribe(val -> publish(backMapper.apply(val)));
        return signal;
    }

    @Override
    public T get() {
        return lastValue;
    }

    @Override
    public C clone() {
        return clonner.get();
    }

    protected C createDistinctSignal() {
        C signal = clone();
        subscribe(value -> {
            if (!Objects.equals(signal.get(), get())) {
                signal.publish(value);
            }
        });
        return signal;
    }

    protected boolean isPublishing() {
        return notifier.isPublishing();
    }
}
