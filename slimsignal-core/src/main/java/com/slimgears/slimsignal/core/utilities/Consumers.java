package com.slimgears.slimsignal.core.utilities;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by denis on 4/7/2017.
 */
public class Consumers {
    public static <T> Consumer<T> filter(Consumer<T> consumer, Predicate<T> predicate) {
        return obj -> {
            if (predicate.test(obj)) {
                consumer.accept(obj);
            }
        };
    }
}
