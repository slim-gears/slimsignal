package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.BoolSignal;
import com.slimgears.slimsignal.core.interfaces.IntSignal;
import com.slimgears.slimsignal.core.interfaces.Signal;
import com.slimgears.slimsignal.core.interfaces.StringSignal;
import com.slimgears.slimsignal.core.utilities.Lazy;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Created by denis on 4/7/2017.
 */
public final class InternalStringSignal extends AbstractInternalComparableSignal<String, InternalStringSignal> implements StringSignal {
    private final Supplier<IntSignal> lengthSignal;

    public InternalStringSignal() {
        super(InternalStringSignal::new);
        lengthSignal = Lazy.of(() -> mapToInt(String::length));
    }

    @Override
    public BoolSignal startsWith(Signal<String> prefix) {
        return forCondition(prefix, String::startsWith);
    }

    @Override
    public BoolSignal endsWith(Signal<String> suffix) {
        return forCondition(suffix, String::endsWith);
    }

    @Override
    public BoolSignal contains(Signal<String> substr) {
        return forCondition(substr, String::contains);
    }

    @Override
    public BoolSignal matches(Signal<Pattern> regex) {
        return forCondition(regex, (val, p) -> p.asPredicate().test(val));
    }

    @Override
    public IntSignal length() {
        return lengthSignal.get();
    }
}
