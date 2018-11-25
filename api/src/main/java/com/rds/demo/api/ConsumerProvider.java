package com.rds.demo.api;

import org.osgi.annotation.versioning.ProviderType;
import org.reactivestreams.Subscriber;

import java.util.Observable;

@ProviderType
public interface ConsumerProvider<T> {

    void process(T event);

}
