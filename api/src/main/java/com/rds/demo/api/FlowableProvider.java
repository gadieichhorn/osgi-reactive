package com.rds.demo.api;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface FlowableProvider<T> {

    Disposable subscribe(Consumer<T> onNext);

}

