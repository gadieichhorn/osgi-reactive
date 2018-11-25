package com.rds.demo.api;

import io.reactivex.Observable;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface EventTopic<T> {

    void publish(T event);

    Observable<T> stream();

}
