package com.rds.demo;

import com.rds.demo.api.Message;
import com.rds.demo.api.MessageBus;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

public class MessageBusTest {

    private final BundleContext context = FrameworkUtil.getBundle(MessageBusTest.class).getBundleContext();

    private ServiceTracker<MessageBus, MessageBus> messageBusMessageBusServiceTracker;

    private MessageBus bus;

    @Before
    public void before() throws InterruptedException {
        messageBusMessageBusServiceTracker = new ServiceTracker<MessageBus, MessageBus>(context, MessageBus.class, null);
        messageBusMessageBusServiceTracker.open();
        bus = messageBusMessageBusServiceTracker.waitForService(50);
        Assert.assertNotNull(bus);
    }

    @After
    public void after() {
        messageBusMessageBusServiceTracker.close();
    }

    @Test
    public void canPublishToBusTest() {
        TestObserver<Message> test = TestObserver.create();
        bus.subscribe(test);
        bus.publish(new TestMessage());
        test.assertValueCount(1);
    }

    @Test
    public void busIsNotCompletedTest() {
        TestObserver<Message> test = TestObserver.create();
        bus.subscribe(test);
        bus.publish(new TestMessage());
        test.assertNotComplete();
    }

    @Test
    public void noErrorsTest() {
        TestObserver<Message> test = TestObserver.create();
        bus.subscribe(test);
        bus.publish(new TestMessage());
        test.assertNoErrors();
    }
}
