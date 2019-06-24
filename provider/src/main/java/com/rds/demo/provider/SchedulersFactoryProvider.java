package com.rds.demo.provider;

import com.rds.demo.api.SchedulersFactory;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.osgi.service.component.annotations.Component;

@Component(service = SchedulersFactory.class)
public class SchedulersFactoryProvider implements SchedulersFactory {

    @Override
    public Scheduler blocking() {
        return Schedulers.io();
    }

    @Override
    public Scheduler pooled() {
        return Schedulers.computation();
    }

}
