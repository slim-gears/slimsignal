/**
 * Copyright (c) 2011-2016 EMC Corporation
 * All Rights Reserved
 * EMC Confidential: Restricted Internal Distribution
 * 4ebcffbc4faf87cb4da8841bbf214d32f045c8a8.ScaleIO
 */
package com.slimgears.slimsignal.core.utilities;

import java.util.Optional;

/**
 * Created by itskod on 03/04/2017.
 */
public class RecursionGuard {
    private final ThreadLocal<Integer> counter = new ThreadLocal<>();
    private final int maxRecursion;

    public RecursionGuard(int maxRecursion) {
        this.maxRecursion = maxRecursion;
    }

    public RecursionGuard() {
        this(1);
    }

    public AutoCloseable lock() {
        int newCounter = Optional
                .ofNullable(counter.get())
                .orElse(0) + 1;
        if (newCounter > maxRecursion) {
            throw new RuntimeException("Recursion limit exceeded was");
        }
        counter.set(newCounter);

        return this::unlock;
    }

    private void unlock() {
        int newCounter = Optional
                .ofNullable(counter.get())
                .orElseThrow(() -> new RuntimeException("Recursive lock integrity error")) - 1;
        if (newCounter == 0) {
            counter.remove();
        } else {
            counter.set(newCounter);
        }
    }

    public boolean isAcquired() {
        return counter.get() != null;
    }
}
