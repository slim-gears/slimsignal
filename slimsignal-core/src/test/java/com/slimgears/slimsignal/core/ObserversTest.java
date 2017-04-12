package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Observable;
import com.slimgears.slimsignal.core.interfaces.Signal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by denis on 4/10/2017.
 */
@RunWith(JUnit4.class)
public class ObserversTest {
    @Test
    public void booleanObserversAnd() {
        Signal<Boolean> arg1 = Signals.create();
        Signal<Boolean> arg2 = Signals.create();
        Signal<Boolean> arg3 = Signals.create();

        Observable<Boolean> and = Observables.and(arg1, arg2, arg3);
        Consumer<Boolean> andConsumer = ConsumerMock.create();
        and.subscribe(andConsumer);

        verify(andConsumer, never()).accept(any());

        arg1.publish(true);
        verify(andConsumer).accept(true);
    }
}
