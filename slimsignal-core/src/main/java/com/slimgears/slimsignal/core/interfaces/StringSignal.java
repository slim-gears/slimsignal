package com.slimgears.slimsignal.core.interfaces;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by denis on 4/7/2017.
 */
public interface StringSignal extends ComparableSignal<String> {
    BoolSignal startsWith(Signal<String> prefix);
    BoolSignal endsWith(Signal<String> suffix);
    BoolSignal contains(Signal<String> substr);
    BoolSignal matches(Signal<Pattern> regex);
    IntSignal length();
}
