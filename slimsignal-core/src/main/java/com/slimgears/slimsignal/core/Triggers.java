package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Trigger;
import com.slimgears.slimsignal.core.internal.DefaultTrigger;

import java.util.stream.Stream;

/**
 * Created by denis on 4/7/2017.
 */
public class Triggers {
    public static Trigger create() {
        return new DefaultTrigger();
    }

    public static Trigger forAny(Trigger... triggers) {
        DefaultTrigger newTrigger = new DefaultTrigger();
        Stream.of(triggers).forEach(t -> t.subscribe(newTrigger::trigger));
        return newTrigger;
    }
}
