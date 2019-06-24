package com.rds.demo.provider;

import com.rds.demo.api.SchedulersFactory;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Allow changing the {@link Schedulers} used for different type of operations.
 */
@Component(
//        configurationPid = "com.rds.reactive.provider.schedulers.provider",
        name = "com.rds.reactive.provider.schedulers.provider",
        service = SchedulersFactory.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(
        ocd = SchedulersFactoryProviderConfiguration.class
)
public class SchedulersFactoryProvider implements SchedulersFactory {

    private Scheduler pooled;
    private Scheduler blocking;

    @Activate
    public void activate(SchedulersFactoryProviderConfiguration cfg) {
//        cfg._blocking()
    }

    @Override
    public Scheduler blocking() {
        return Schedulers.io();
    }

    @Override
    public Scheduler pooled() {
        return Schedulers.computation();
    }

}
