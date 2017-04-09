package com.slimgears.slimsignal.core;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;

/**
 * Created by denis on 4/7/2017.
 */
public class ConsumerMock {
    public static <T> Consumer<T> create() {
        //noinspection unchecked
        return (Consumer<T>)mock(Consumer.class);
    }
}
