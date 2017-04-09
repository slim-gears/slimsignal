package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.DoubleSignal;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalDoubleSignal extends AbstractInternalNumericSignal<Double, InternalDoubleSignal> implements DoubleSignal {
    public InternalDoubleSignal() {
        super(InternalDoubleSignal::new);
    }
}
