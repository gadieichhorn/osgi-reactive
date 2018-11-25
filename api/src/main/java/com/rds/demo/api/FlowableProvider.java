package com.rds.demo.api;

import io.reactivex.Flowable;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface FlowableProvider<T> {

    Flowable<T> source();

}

