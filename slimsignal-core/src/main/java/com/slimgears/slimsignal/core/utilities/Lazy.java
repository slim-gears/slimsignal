package com.slimgears.slimsignal.core.utilities;

import java.util.function.Supplier;

/**
 * Created by denis on 4/7/2017.
 */
public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private volatile T value;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }
        return value;
    }

    public static <T> Supplier<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
}
