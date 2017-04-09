package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.internal.InternalTrigger;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class Triggers {
    public static Trigger create() {
        return new InternalTrigger();
    }

    public static Trigger forAny(Trigger... triggers) {
        Trigger newTrigger = create();
        Stream.of(triggers).forEach(t -> t.subscribe(newTrigger::trigger));
        return newTrigger;
    }
}
