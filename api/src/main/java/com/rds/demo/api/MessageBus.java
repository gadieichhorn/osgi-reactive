package com.rds.demo.api;

import io.reactivex.Observer;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface MessageBus<T> {

    void publish(T message);

    void subscribe(Observer<T> observer);

}
