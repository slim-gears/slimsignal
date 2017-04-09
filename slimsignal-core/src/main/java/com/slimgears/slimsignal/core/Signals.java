package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.*;
import com.slimgears.slimsignal.core.internal.*;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class Signals {
    public static <T> Signal<T> create() {
        return new InternalSignal<>();
    }

    public static <T> Signal<T> create(Consumer<T> subscriber) {
        Signal<T> signal = new InternalSignal<>();
        signal.subscribe(subscriber);
        return signal;
    }

    public static Trigger triggerForAny(Signal... signals) {
        Trigger trigger = Triggers.create();
        Consumer subscriber = obj -> trigger.trigger();
        //noinspection unchecked
        Stream.of(signals).forEach(signal -> signal.subscribe(subscriber));
        return trigger;
    }

    public static <T> CollectionSignal<T> collection() {
        return new InternalCollectionSignal<>();
    }

    public static BoolSignal boolSignal() {
        return new InternalBoolSignal();
    }

    public static BoolSignal of(boolean value) {
        return fromConstant(Signals::boolSignal, value);
    }

    public static IntSignal intSignal() {
        return new InternalIntSignal();
    }


    public static IntSignal of(int value) {
        return fromConstant(Signals::intSignal, value);
    }

    public static LongSignal longSignal() {
        return new InternalLongSignal();
    }

    public static LongSignal of(long value) {
        return fromConstant(Signals::longSignal, value);
    }

    public static DoubleSignal doubleSignal() {
        return new InternalDoubleSignal();
    }

    public static DoubleSignal of(double value) {
        return fromConstant(Signals::doubleSignal, value);
    }

    public static FloatSignal floatSignal() {
        return new InternalFloatSignal();
    }

    public static FloatSignal of(float value) {
        return fromConstant(Signals::floatSignal, value);
    }

    private static <T, S extends Signal<T>> S fromConstant(Supplier<S> signalSupplier, T value) {
        S signal = signalSupplier.get();
        signal.publish(value);
        return signal;
    }
}
