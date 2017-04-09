/**
 * Copyright (c) 2011-2016 EMC Corporation
 * All Rights Reserved
 * EMC Confidential: Restricted Internal Distribution
 * 4ebcffbc4faf87cb4da8841bbf214d32f045c8a8.ScaleIO
 */
package com.slimgears.slimsignal.core.internal;

/**
 * Created by denis on 4/1/2017.
 */
public final class InternalSignal<T, C extends InternalSignal<T, C>> extends AbstractInternalSignal<T, C> {

    public InternalSignal() {
        //noinspection unchecked
        super(() -> (C)new InternalSignal<>());
    }
}
