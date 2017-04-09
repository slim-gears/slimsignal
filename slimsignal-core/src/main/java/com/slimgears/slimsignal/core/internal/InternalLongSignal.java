package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.LongSignal;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalLongSignal extends AbstractInternalNumericSignal<Long, InternalLongSignal> implements LongSignal {
    public InternalLongSignal() {
        super(InternalLongSignal::new);
    }
}
