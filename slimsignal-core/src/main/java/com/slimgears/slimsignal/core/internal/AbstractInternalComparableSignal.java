/**
 * Copyright (c) 2011-2016 EMC Corporation
 * All Rights Reserved
 * EMC Confidential: Restricted Internal Distribution
 * 4ebcffbc4faf87cb4da8841bbf214d32f045c8a8.ScaleIO
 */
package com.slimgears.slimsignal.core.internal;

import com.slimgears.slimsignal.core.interfaces.BoolSignal;
import com.slimgears.slimsignal.core.interfaces.ComparableSignal;
import com.slimgears.slimsignal.core.interfaces.Signal;

import java.util.Comparator;
import java.util.function.Supplier;

/**
 * Created by itskod on 05/04/2017.
 */
public abstract class AbstractInternalComparableSignal<T extends Comparable<T>, C extends AbstractInternalComparableSignal<T, C>> extends AbstractInternalSignal<T, C> implements ComparableSignal<T> {
    private final Comparator<T> comparator = Comparator.naturalOrder();

    protected AbstractInternalComparableSignal(Supplier<C> clonner) {
        super(clonner);
    }

    @Override
    public BoolSignal lessThan(Signal<T> other) {
        return forCondition(other, (T val1, T val2) -> comparator.compare(val1, val2) < 0);
    }

    @Override
    public BoolSignal lessOrEqual(Signal<T> other) {
        return forCondition(other, (T val1, T val2) -> comparator.compare(val1, val2) <= 0);
    }

    @Override
    public BoolSignal greaterThan(Signal<T> other) {
        return forCondition(other, (T val1, T val2) -> comparator.compare(val1, val2) > 0);
    }

    @Override
    public BoolSignal greaterOrEqual(Signal<T> other) {
        return forCondition(other, (T val1, T val2) -> comparator.compare(val1, val2) >= 0);
    }
}
