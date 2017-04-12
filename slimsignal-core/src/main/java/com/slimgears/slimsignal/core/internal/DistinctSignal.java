package com.slimgears.slimsignal.core.internal;

import java.util.Objects;

/**
 * Created by denis on 4/10/2017.
 */
public class DistinctSignal<T> extends DefaultSignal<T> {
    public void publish(T value) {
        if (!Objects.equals(get(), value)) {
            super.publish(value);
        }
    }
}
