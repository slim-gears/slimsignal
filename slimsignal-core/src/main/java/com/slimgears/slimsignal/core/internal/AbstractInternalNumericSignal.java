/**
 * Copyright (c) 2011-2016 EMC Corporation
 * All Rights Reserved
 * EMC Confidential: Restricted Internal Distribution
 * 4ebcffbc4faf87cb4da8841bbf214d32f045c8a8.ScaleIO
 */
package com.slimgears.slimsignal.core.internal;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created by itskod on 05/04/2017.
 */
public abstract class AbstractInternalNumericSignal<T extends Comparable<T>, C extends AbstractInternalNumericSignal<T, C>> extends AbstractInternalComparableSignal<T, C> {
    protected AbstractInternalNumericSignal(Supplier<C> clonner) {
        super(clonner);
    }

    @Override
    public void publish(T value) {
        if (!Objects.equals(get(), value)) {
            super.publish(value);
        }
    }
}
