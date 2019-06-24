# RxJava message bus for OSGi 

I wanted to make OSGi components talk in a rich event driven way where I can publish many different messages between 
components without tight coupling or direct @Reference(ing). The OSGI EventAdmin is a nice way to start but sooner or 
later you run into limitations.

The drive was to make a system that is more Reactive and easy to understand but at the same time not limiting.

I chose to use RxJava as the underlying engine for delivering messages because:
* It is fast (async)
* It is simple
* It is powerful with operations & schedulers
* it has great unit test tools build in.

I did have few challenges on the way, mainly making a very clean API that will be easy to share and ruse and not 
complicated to understand. I also needed a good way to unit test my components and during OSGi integration tests.

As we will see some tests are a bit more tricky then others.

I got inspiration from this blog which I recommend reading as I am not going to repeat what Paulina described. 
I decided to port the implementation to OSGi and create a small configuration abstraction.

https://medium.com/@PaulinaSadowska/writing-unit-tests-on-asynchronous-events-with-rxjava-and-rxkotlin-1616a27f69aa

## A simple bus implementation
A very simple message bus with a generic Message type as a payload. Clients are required to implement messages of 
this type:

```java
import com.rds.demo.api.Message;

public class MyMessage implements Message {
}

```

Now we need to get instance of the MessageBus so we can start communicating with other components using RxJava
The Message bus allow two main methods to consume and produce messages on the bus.

```java
public interface MessageBus<T> {

    void publish(T message);

    void subscribe(Observer<T> observer);

}

```

The default MessageBusProvider then implement this service and deploy it as OSGi service. Internally using the 
PublishSubject that allows dynamically sending messages and subscribing to them as a hot Observable.

Pay attention to the SINGLETON annotation making sure there is only one Bus instance in the OSGi registration.


```java
@Component(service = MessageBus.class, scope = ServiceScope.SINGLETON)
public class MessageBusProvider<T extends Message> implements MessageBus<T> {

    private final PublishSubject<T> publishSubject = PublishSubject.create();

    @Override
    public void publish(T message) {
        publishSubject.onNext(message);
    }

    @Override
    public void subscribe(Observer<T> observer) {
        publishSubject.subscribe(observer);
    }

}

```

Now if we want to test the new service directly (unit test not OSGi integration tests) we can use the RxJava test 
abstractions like so:

```java

@Test
public void publishMessageTest() {
    TestObserver<Message> test = TestObserver.create();

    MessageBus<Message> bus = new MessageBusProvider<>();

    bus.subscribe(test);
    bus.publish(new TestMessage());

    test.assertValueCount(1);
    test.assertNoErrors();
    test.assertNotComplete();
}
```

## Step two, Schedulers
That was easy, right? Well not so fast... we forgot Schedulers. RxJava Schedulers allow running pipes on different 
threads. This is the real power of RxJava when it comes to doing things in parallel.

So lets add a computation scheduler to the mix:

```java
    @Override
    public void subscribe(Observer<T> observer) {
        publishSubject
        .subscribeOn(schedulers.computation())
        .subscribe(observer);
    }
```

But now we have a problem as our Observers are listening on the computation thread pool our testObserver 
will not work anymore as it will complete without waiting.

To overcome this problem we need to use a different Scheduler inside our tests but our MessageBus service is not 
allowing us to change anything so we have a problem.

To extends the MessageBus one option is to allow injection of another service that will control the Schedulers. 
As Paulina outlined, this can be an abstraction of the Schedulers as a service. SIne we are not concern with Android 
UI I made some changes to the API.

```java
public interface SchedulersFactory {

    Scheduler blocking();

    Scheduler pooled();

}
```

Injecting this service into our MessageBus service we can now change the code to be:

```java
@Component(service = MessageBus.class, scope = ServiceScope.SINGLETON)
public class MessageBusProvider<T extends Message> implements MessageBus<T> {

    private final PublishSubject<T> publishSubject = PublishSubject.create();

    @Reference
    private SchedulersFactory schedulersFactory;

    public MessageBusProvider() {
    }

    public MessageBusProvider(SchedulersFactory schedulersFactory) {
        this.schedulersFactory = schedulersFactory;
    }

    @Override
    public void publish(T message) {
        publishSubject.onNext(message);
    }

    @Override
    public void subscribe(Observer<T> observer) {
        publishSubject.subscribeOn(schedulersFactory.pooled()).subscribe(observer);
    }

}
```

The default constructor is needed for the OSGi framework to create instance of our class.
The Other constructor will allow us to inject a different SchedulerFactory in our tests.

```java
public class TestSchedulersFactory implements SchedulersFactory {

    @Override
    public Scheduler pooled() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler blocking() {
        return Schedulers.trampoline();
    }

}
```

The trampoline Scheduler will fix our problem as it perform operations in sequence so our test observer will be tested 
only after all the messages were processed.

Now we can change the using tests to inject a different SchdulersFactory instance.

```java
       MessageBus<Message> bus = new MessageBusProvider<>(new TestSchedulersFactory());
```

All good for now.

## Integration tests
OSGI testing is considered integration tests as it attempt to build a full runtime and inject unit test wrapper into 
the runtime, then run assertions. This allows us to build small parts of our application, adapt the configuration, and 
test everything is wired properly. But are we forgetting something? Yes, test will still fail as we didn't plan for 
SchedulerFactory configuration.

Let's fix that.

First we need to allow configuration on our SchedulersFactory, our component will now have a configuration interface

```java
public @interface SchedulersFactoryProviderConfiguration {

    @AttributeDefinition(
            name = ".blocking",
            type = AttributeType.STRING,
            description = "Blocking scheduler",
            required = false,
            options = {
                    @Option(label = "io", value = "IO"),
                    @Option(label = "computation", value = "COMPUTATION")
            }
    )
    String _blocking() default "IO";

    @AttributeDefinition(
            name = ".pooled",
            type = AttributeType.STRING,
            description = "Pooled scheduler",
            required = false,
            options = {
                    @Option(label = "io", value = "IO"),
                    @Option(label = "computation", value = "COMPUTATION")
            }
    )
    String _pooled() default "COMPUTATION";

}
```

We can then inject this configuration to our component in the activate method

```java
@Component(
        name = "com.rds.reactive.provider.schedulers.provider",
        service = SchedulersFactory.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(
        ocd = SchedulersFactoryProviderConfiguration.class
)
public class SchedulersFactoryProvider implements SchedulersFactory {

    private Scheduler pooled;
    private Scheduler blocking;

    @Activate
    public void activate(SchedulersFactoryProviderConfiguration cfg) {
        System.out.println("ACTIVATE");
        blocking = SchedulerType.get(cfg._blocking()).value();
        pooled = SchedulerType.get(cfg._pooled()).value();
    }

    @Override
    public Scheduler blocking() {
        return blocking;
    }

    @Override
    public Scheduler pooled() {
        return pooled;
    }

}
```

And with a small helper enum we are making sure we have the right Schedulers defined.

```java
public enum SchedulerType {

    IO(Schedulers.io()),
    COMPUTATION(Schedulers.computation()),
    TRAMPOLINE(Schedulers.trampoline());

    private final Scheduler scheduler;

    SchedulerType(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    Scheduler value() {
        return scheduler;
    }

    private static final Map<String, SchedulerType> ENUM_MAP;

    static {
        Map<String, SchedulerType> map = new HashMap<>();
        for (SchedulerType instance : SchedulerType.values()) {
            map.put(instance.name(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static SchedulerType get(String name) {
        return ENUM_MAP.getOrDefault(name, COMPUTATION);
    }
}

```

Our SchedulerFactory is injected into the MessageBus using a @Reference (OSGi DS). 

```java
    @Reference
    private SchedulersFactory schedulersFactory;
```

One last thing, we need to tell OSGi framework how to configure our SchedulersFactory in test mode as we still need to 
use the trampoline scheduler in our tests.

In our config folder (see rest.runbnd) we link the config folder to our runtime 
```properties
-runproperties: \
	felix.fileinstall.dir=${.}/config

```

Add the configuration properties file to this folder:
> config/com.rds.reactive.provider.schedulers.provider.cfg

```properties
.blocking=TRAMPOILNE
.pooled=TRAMPOLINE
```

And now for our integration tests... we need to get an instance of the MessageBus from the framework, this instance 
will use the SchedulersFactory instance with our TRAMPOLINE override configuration so we can have a predictable working 
tests.

```java
public class MessageBusTest {

    private final BundleContext context = FrameworkUtil.getBundle(MessageBusTest.class).getBundleContext();

    private ServiceTracker<MessageBus, MessageBus> messageBusMessageBusServiceTracker;

    private MessageBus bus;

    @Before
    public void before() throws InterruptedException {
        messageBusMessageBusServiceTracker = new ServiceTracker<MessageBus, MessageBus>(context, MessageBus.class, null);
        messageBusMessageBusServiceTracker.open();
        bus = messageBusMessageBusServiceTracker.waitForService(500);
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
        test.assertNotComplete();
        test.assertNoErrors();
    }

    @Test
    public void multipleMessagesTest() {
        TestObserver<Message> test = TestObserver.create();
        bus.subscribe(test);

        // it is critical to set the scheduler here otherwise it is using the inmternal computation scheduler.
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.trampoline())
                .take(10)
                .map(t -> new TestMessage())
                .subscribe(testMessage -> bus.publish(testMessage));

        test.assertValueCount(10);
        test.assertNotComplete();
        test.assertNoErrors();
    }

}
```
Notice we ask for the MessageBus from the context and that is it. The service was configured for us using the cfg file.

Also notice the interval Observer is using a scheduler. If you not giving the interval a scheduler it will default to 
computation and break our tests.

```java
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.trampoline())
```

## Summary
RxJAva gives us powerful tools for building and testing applications but we need to be careful when testing them.

I hope this was helpful if you are working with OSGI or RxJava.


## The code
Please feel free to use and port or contribute.

https://github.com/gadieichhorn/osgi-reactive

## References

Paulina's blog post -> 
https://medium.com/@PaulinaSadowska/writing-unit-tests-on-asynchronous-events-with-rxjava-and-rxkotlin-1616a27f69aa

EffectiveOSGi -> 

