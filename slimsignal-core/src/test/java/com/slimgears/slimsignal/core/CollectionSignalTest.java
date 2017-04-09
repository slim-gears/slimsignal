package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.CollectionSignal;
import com.slimgears.slimsignal.core.interfaces.StringSignal;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

/**
 * Created by denis on 4/7/2017.
 */
@RunWith(JUnit4.class)
public class CollectionSignalTest {
    @Test
    public void testCollection() {
        CollectionSignal<Integer> collection = Signals.collection();
        collection.addAll(Arrays.asList(1, 2, 4, 8, 16, 32));

        Consumer<Integer> sizeConsumer = ConsumerMock.create();
        collection.size().subscribe(sizeConsumer);

        verify(sizeConsumer).accept(6);

        StringSignal joinedString = collection
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                .mapToString(val -> val);

        Assert.assertEquals("1, 2, 4, 8, 16, 32", joinedString.get());

        Consumer<String> joinedStringConsumer = ConsumerMock.create();
        joinedString.subscribe(joinedStringConsumer);

        verify(joinedStringConsumer).accept("1, 2, 4, 8, 16, 32");
        clearInvocations(joinedStringConsumer);
        collection.add(64);

        verify(joinedStringConsumer).accept("1, 2, 4, 8, 16, 32, 64");
    }

    @Test
    public void testReactiveX() {
    }
}
