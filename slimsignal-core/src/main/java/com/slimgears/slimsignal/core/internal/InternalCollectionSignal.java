package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.Signals;
import com.slimgears.slimsignal.core.Triggers;
import com.slimgears.slimsignal.core.interfaces.*;
import com.slimgears.slimsignal.core.utilities.Lazy;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalCollectionSignal<T> implements CollectionSignal<T> {
    private final Map<T, Signal<T>> items = new LinkedHashMap<>();
    private final IntSignal sizeSignal = Signals.intSignal();
    private final BoolSignal isEmptySignal = Signals.boolSignal();
    private final Supplier<Signal<T>> addedSignal;
    private final Supplier<Signal<T>> removedSignal;
    private final Supplier<Trigger> trigger;

    public InternalCollectionSignal() {
        addedSignal = Lazy.of(() -> Signals.create(this::addItem));
        removedSignal = Lazy.of(() -> Signals.create(this::removeItem));
        trigger = Lazy.of(() -> Signals.triggerForAny(added(), removed()));
    }

    @Override
    public Iterable<Signal<T>> items() {
        return items.values();
    }

    @Override
    public IntSignal size() {
        return sizeSignal;
    }

    @Override
    public Signal<T> added() {
        return addedSignal.get();
    }

    @Override
    public Signal<T> removed() {
        return removedSignal.get();
    }

    @Override
    public Signal<T> item(T item) {
        return items.get(item);
    }

    @Override
    public BoolSignal contains(Signal<T> item) {
        Trigger trigger = Triggers.forAny(asTrigger(), item.asTrigger());
        BoolSignal containsSignal = Signals.boolSignal();
        trigger.subscribe(() -> containsSignal.publish(items.containsKey(item.get())));
        return containsSignal;
    }

    @Override
    public BoolSignal isEmpty() {
        return isEmptySignal;
    }

    @Override
    public Trigger asTrigger() {
        return trigger.get();
    }

    @Override
    public CollectionSignal<T> filter(Predicate<T> predicate) {
        CollectionSignal<T> collection = clone();
        added().filter(predicate).subscribe(collection::add);
        removed().filter(predicate).subscribe(collection::remove);
        return collection;
    }

    @Override
    public <R> CollectionSignal<R> map(Function<T, R> mapper) {
        //noinspection unchecked
        CollectionSignal<R> collection = (CollectionSignal<R>)clone();
        added().subscribe(item -> collection.add(mapper.apply(item)));
        removed().subscribe(item -> collection.remove(mapper.apply(item)));
        return collection;
    }

    @Override
    public <R, A> Signal<R> collect(Collector<? super T, A, R> collector) {
        Signal<R> signal = Signals.create();
        asTrigger().subscribe(() -> signal.publish(items.keySet().stream().collect(collector)));
        return signal;
    }

    @Override
    public void clear() {
        items.clear();
        publishUpdate();
    }

    private void addItem(T item) {
        items.put(item, createItemSignal(item));
        publishUpdate();
    }

    private void removeItem(T item) {
        items.remove(item);
        publishUpdate();
    }

    private Signal<T> createItemSignal(T item) {
        Signal<T> itemSignal = Signals.<T>create().distinct();
        itemSignal.publish(item);
        itemSignal.subscribe(newVal -> {
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
    public CollectionSignal<T> clone() {
        return new InternalCollectionSignal<>();
    }
}
