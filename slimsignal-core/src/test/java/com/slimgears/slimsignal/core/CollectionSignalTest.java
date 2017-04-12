package com.slimgears.slimsignal.core;

import com.slimgears.slimsignal.core.interfaces.Observable;
import com.slimgears.slimsignal.core.interfaces.ObservableCollection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by denis on 4/7/2017.
 */
@RunWith(JUnit4.class)
public class CollectionSignalTest {
    @Test
    public void observableCollectionMapAndCollect() {
        ObservableCollection<Integer> collection = Observables.collection();
        collection.addAll(Arrays.asList(1, 2, 4, 8, 16, 32));

        Consumer<Integer> sizeConsumer = ConsumerMock.create();
        collection.size().subscribe(sizeConsumer);

        verify(sizeConsumer).accept(6);

        Observable<String> joinedString = collection
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        Assert.assertEquals("1, 2, 4, 8, 16, 32", Signals.toSignal(joinedString).get());

        Consumer<String> joinedStringConsumer = ConsumerMock.create();
        joinedString.subscribe(joinedStringConsumer);

        verify(joinedStringConsumer).accept("1, 2, 4, 8, 16, 32");
        collection.add(64);

        verify(joinedStringConsumer).accept("1, 2, 4, 8, 16, 32, 64");
    }

    @Test
    public void observableCollectionFlatMapAndCollect() {
        ObservableCollection<String> collection = Observables.collection();
        collection.addAll(Arrays.asList(
                "Lorem ipsum dolor sit amet",
                "consectetur adipiscing elit",
                "In tempus justo"));

        Observable<String> joinedString = collection
                .flatMap(str -> Arrays.asList(str.split("\\s+")))
                .collect(Collectors.joining(","));

        Consumer<String> joinedStringConsumer = ConsumerMock.create();
        joinedString.subscribe(joinedStringConsumer);

        verify(joinedStringConsumer).accept("Lorem,ipsum,dolor,sit,amet,consectetur,adipiscing,elit,In,tempus,justo");

        collection.add("Hello world");
        verify(joinedStringConsumer).accept("Lorem,ipsum,dolor,sit,amet,consectetur,adipiscing,elit,In,tempus,justo,Hello,world");
    }

    @Test
    public void observableCollectionIsEmptyAndSizeSignals() {
        ObservableCollection<Integer> collection = Observables.collection();

        Consumer<Boolean> isEmptyConsumer = ConsumerMock.create();
        Consumer<Integer> sizeConsumer = ConsumerMock.create();

        collection.isEmpty().subscribe(isEmptyConsumer);
        collection.size().subscribe(sizeConsumer);

        verify(isEmptyConsumer).accept(true);
        verify(sizeConsumer).accept(0);

        collection.addAll(Arrays.asList(1, 2, 3, 4));

        verify(isEmptyConsumer, times(1)).accept(false);
        verify(sizeConsumer).accept(4);

        collection.clear();

        verify(isEmptyConsumer, times(2)).accept(true);
        verify(sizeConsumer, times(2)).accept(0);
    }
}
