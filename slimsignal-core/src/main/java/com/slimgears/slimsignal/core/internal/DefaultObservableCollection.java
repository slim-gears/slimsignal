package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.Observables;
import com.slimgears.slimsignal.core.Signals;
import com.slimgears.slimsignal.core.Triggers;
import com.slimgears.slimsignal.core.interfaces.*;
import com.slimgears.slimsignal.core.utilities.Lazy;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

/**
 * Created by denis on 4/7/2017.
 */
public final class DefaultObservableCollection<T> implements ObservableCollection<T> {
    private final Map<T, Observable<T>> items = new LinkedHashMap<>();
    private final Signal<Integer> sizeSignal = Signals.<Integer>create().distinct();
    private final Signal<Boolean> isEmptySignal = Signals.<Boolean>create().distinct();
    private final Signal<T> addedSignal;
    private final Signal<T> removedSignal;
    private final Supplier<Trigger> trigger;

    public DefaultObservableCollection() {
        addedSignal = createAddedSignal();
        removedSignal = Signals.create();
        trigger = Lazy.of(() -> Observables.triggerForAny(added(), removed()));
        publishUpdate();
    }

    private Signal<T> createAddedSignal() {
        return new AbstractSignal<T>() {
            @Override
            public T get() {
                return null;
            }

            @Override
            public synchronized Subscription subscribe(Consumer<T> subscriber) {
                items.keySet().forEach(subscriber);
                return super.subscribe(subscriber);
            }

            @Override
            protected <R> Signal<R> createSignal() {
                return Signals.create();
            }
        };
    }

    @Override
    public Iterable<Observable<T>> items() {
        return items.values();
    }

    @Override
    public Observable<Integer> size() {
        return sizeSignal;
    }

    @Override
    public Observable<T> added() {
        return addedSignal;
    }

    @Override
    public Observable<T> removed() {
        return removedSignal;
    }

    @Override
    public Observable<T> item(T item) {
        return items.get(item);
    }

    @Override
    public Observable<Boolean> contains(Observable<T> item) {
        Signal<T> itemSignal = Signals.toSignal(item);
        Trigger trigger = Triggers.forAny(asTrigger(), itemSignal.asTrigger());
        Signal<Boolean> containsSignal = Signals.create();
        trigger.subscribe(() -> containsSignal.publish(items.containsKey(itemSignal.get())));
        return containsSignal;
    }

    @Override
    public Observable<Boolean> isEmpty() {
        return isEmptySignal;
    }

    @Override
    public Trigger asTrigger() {
        return trigger.get();
    }

    @Override
    public ObservableCollection<T> filter(Predicate<T> predicate) {
        ObservableCollection<T> collection = clone();
        added().filter(predicate).subscribe(collection::add);
        removed().filter(predicate).subscribe(collection::remove);
        return collection;
    }

    @Override
    public <R> ObservableCollection<R> map(Function<T, R> mapper) {
        //noinspection unchecked
        ObservableCollection<R> collection = (ObservableCollection<R>)clone();
        addedSignal.subscribe(item -> collection.add(mapper.apply(item)));
        removedSignal.subscribe(item -> collection.remove(mapper.apply(item)));
        return collection;
    }

    @Override
    public <R> ObservableCollection<R> flatMap(Function<T, Iterable<R>> mapper) {
        //noinspection unchecked
        ObservableCollection<R> collection = (ObservableCollection<R>)clone();
        addedSignal.subscribe(item -> StreamSupport.stream(mapper.apply(item).spliterator(), false).forEach(collection::add));
        removedSignal.subscribe(item -> StreamSupport.stream(mapper.apply(item).spliterator(), false).forEach(collection::remove));
        return collection;
    }

    @Override
    public <R, A> Observable<R> collect(Collector<? super T, A, R> collector) {
        Signal<R> signal = Signals.create();
        Runnable listener = () -> signal.publish(items.keySet().stream().collect(collector));
        asTrigger().subscribe(listener);
        listener.run();
        return signal;
    }

    @Override
    public void clear() {
        removeAll(items.keySet());
    }

    @Override
    public void add(T item) {
        items.put(item, createItemSignal(item));
        addedSignal.publish(item);
        publishUpdate();
    }

    @Override
    public void remove(T item) {
        if (items.remove(item) != null) {
            removedSignal.publish(item);
            publishUpdate();
        }
    }

    private Signal<T> createItemSignal(T item) {
        Signal<T> itemSignal = Signals.create();
        itemSignal.publish(item);
        itemSignal.distinct().subscribe(newVal -> {
            items.remove(item);
            items.put(newVal, itemSignal);
        });
        return itemSignal;
    }

    private void publishUpdate() {
        sizeSignal.publish(items.size());
        isEmptySignal.publish(items.isEmpty());
    }

    @Override
    public Collection<T> get() {
        return items.keySet();
    }

    @Override
    public ObservableCollection<T> clone() {
        return new DefaultObservableCollection<>();
    }
}
