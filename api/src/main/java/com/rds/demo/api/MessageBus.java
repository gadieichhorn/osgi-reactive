package com.rds.demo.api;

import io.reactivex.Observer;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A Hot {@link io.reactivex.Observable} bus.
 *
 * @param <T> typically a {@link Message} type
 */
@ProviderType
public interface MessageBus<T> {

    /**
     * Push a new message to the bus
     *
     * @param message
     */
    void publish(T message);

    /**
     * Allow subscribers to get messages using the {@link Observer} abstraction.
     *
     * @param observer the {@link Observer} to be bushed new messages.
     */
    void subscribe(Observer<T> observer);

}
