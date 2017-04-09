package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.IntSignal;

import java.util.function.Predicate;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalIntSignal extends AbstractInternalNumericSignal<Integer, InternalIntSignal> implements IntSignal {
    public InternalIntSignal() {
        super(InternalIntSignal::new);
    }
}
