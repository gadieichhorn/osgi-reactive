package com.rds.demo.api;

import org.osgi.annotation.versioning.ProviderType;
import org.reactivestreams.Subscriber;

import java.util.Observable;

@ProviderType
public interface Consumer<T> {

    Subscriber<T> stream();

}
