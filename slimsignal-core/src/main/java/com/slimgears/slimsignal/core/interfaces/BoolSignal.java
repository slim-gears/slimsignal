package com.slimgears.slimsignal.core.interfaces;

/**
 * Created by denis on 4/7/2017.
 */
public interface BoolSignal extends Signal<Boolean> {
    BoolSignal and(Signal<Boolean> boolSignal);
    BoolSignal or(Signal<Boolean> boolSignal);
    BoolSignal invert();
}
