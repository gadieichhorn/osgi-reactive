package com.rds.demo.provider;

import com.rds.demo.api.Message;
import com.rds.demo.api.MessageBus;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

public class MessageBusProviderTest {

    @Test
    public void publishMessageTest() {
        TestObserver<Message> test = TestObserver.create();

        MessageBus<Message> bus = new MessageBusProvider<>(new TestSchedulersFactory());

        bus.subscribe(test);
        bus.publish(new TestMessage());

        test.assertValueCount(1);
        test.assertNoErrors();
        test.assertNotComplete();
    }

}