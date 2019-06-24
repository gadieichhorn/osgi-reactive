package com.rds.demo.provider;

import com.rds.demo.api.SchedulersFactory;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestSchedulersFactory implements SchedulersFactory {

    @Override
    public Scheduler pooled() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler blocking() {
        return Schedulers.trampoline();
    }

}
