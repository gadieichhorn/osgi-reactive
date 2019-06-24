package com.rds.demo.api;

import io.reactivex.Scheduler;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface SchedulersFactory {

    Scheduler blocking();

    Scheduler pooled();

}
