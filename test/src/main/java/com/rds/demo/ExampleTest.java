package com.rds.demo;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import java.util.Observable;

import static org.junit.Assert.*;

public class ExampleTest {

    private final BundleContext context = FrameworkUtil.getBundle(ExampleTest.class).getBundleContext();

    @Test
    public void testExample() {
        Flowable instance = Flowable.just("Hello world");

        instance.subscribe(System.out::println);
        instance.subscribe(System.out::println);
        instance.subscribe(System.out::println);
    }

    @Test
    public void testBackpressure() {
        Flowable<Integer> flow = Flowable.range(1, 5)
                .map(v -> v * v)
                .filter(v -> v % 3 == 0);

        flow.subscribe(System.out::println);
        flow.subscribe(System.out::println);
        flow.subscribe(System.out::println);
        flow.subscribe(System.out::println);
    }

    @Test
    public void testRuntime() {
        Flowable.range(1, 10)
                .flatMap(v ->
                        Flowable.just(v)
                                .subscribeOn(Schedulers.computation())
                                .map(w -> w * w)
                )
                .blockingSubscribe(System.out::println);
    }

}
