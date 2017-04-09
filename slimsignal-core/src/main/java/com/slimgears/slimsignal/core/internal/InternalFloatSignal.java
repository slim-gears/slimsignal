package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.FloatSignal;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalFloatSignal extends AbstractInternalNumericSignal<Float, InternalFloatSignal> implements FloatSignal {
    public InternalFloatSignal() {
        super(InternalFloatSignal::new);
    }
}
