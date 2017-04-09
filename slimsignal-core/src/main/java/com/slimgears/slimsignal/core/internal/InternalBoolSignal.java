package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.BoolSignal;
import com.slimgears.slimsignal.core.interfaces.Signal;
import com.slimgears.slimsignal.core.utilities.Lazy;

import java.util.function.Supplier;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalBoolSignal extends AbstractInternalNumericSignal<Boolean, InternalBoolSignal> implements BoolSignal {
    private final Supplier<BoolSignal> invertedSignal;

    public InternalBoolSignal() {
        super(InternalBoolSignal::new);
        invertedSignal = Lazy.of(() -> mapToBool(val -> !val));
    }

    @Override
    public BoolSignal and(Signal<Boolean> boolSignal) {
        return forCondition(boolSignal, Boolean::logicalAnd);
    }

    @Override
    public BoolSignal or(Signal<Boolean> boolSignal) {
        return forCondition(boolSignal, Boolean::logicalOr);
    }

    @Override
    public BoolSignal invert() {
        return invertedSignal.get();
    }
}
