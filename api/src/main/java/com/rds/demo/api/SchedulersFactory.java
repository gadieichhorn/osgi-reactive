package com.rds.demo.api;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SchedulersFactory {

    /**
     * In RxJava calling this {@link Schedulers} io()
     * Used for blocking oprtations like calling the database
     *
     * @return a {@link Scheduler}
     */
    Scheduler blocking();

    /**
     * In RxJava calling this {@link Schedulers} computation()
     * Anything that is using the local CPU like standard processing
     *
     * @return a {@link Scheduler}
     */
    Scheduler pooled();

}
