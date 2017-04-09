package com.slimgears.slimsignal.core.utilities;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class WeakCollection<T> implements Collection<T> {
    private final List<WeakReference<T>> items = new LinkedList<>();
    private final ReferenceQueue<T> referenceQueue = new ReferenceQueue<>();

    @Override
    public int size() {
        clean();
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        clean();
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.stream().anyMatch(ref -> Objects.equals(ref.get(), o));
    }

    @Override
    public Iterator<T> iterator() {
        return items.stream().map(WeakReference::get).iterator();
    }

    @Override
    public Object[] toArray() {
        //noinspection StreamToLoop
        return stream().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return stream().limit(a.length).toArray(size -> a);
    }

    @Override
    public boolean add(T val) {
        WeakReference<T> ref = new WeakReference<>(val, referenceQueue);
        clean();
        return items.add(ref);
    }

    @Override
    public boolean remove(Object val) {
        clean();
        return items.removeIf(ref -> val.equals(ref.get()));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Boolean[] values = c.stream().map(this::add).toArray(Boolean[]::new);
        return Stream.of(values).anyMatch(Boolean::booleanValue);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Boolean[] values = c.stream().map(this::remove).toArray(Boolean[]::new);
        return Stream.of(values).anyMatch(Boolean::booleanValue);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return items.removeIf(value -> !c.contains(value.get()));
    }

    @Override
    public void clear() {
        items.clear();
    }

    public Stream<T> stream() {
        clean();
        return items.stream().map(WeakReference::get).filter(Objects::nonNull);
    }

    private void clean() {
        while (true) {
            //noinspection unchecked
            WeakReference<T> ref = (WeakReference<T>) referenceQueue.poll();
            if (ref == null) {
                break;
            }
            items.remove(ref);
        }
    }
}
