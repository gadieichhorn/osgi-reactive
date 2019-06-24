package com.rds.demo.provider;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SchedulerType {

    IO(Schedulers.io()),
    COMPUTATION(Schedulers.computation()),
    TRAMPOLINE(Schedulers.trampoline());

    private final Scheduler scheduler;

    SchedulerType(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    Scheduler value() {
        return scheduler;
    }

    private static final Map<String, SchedulerType> ENUM_MAP;

    static {
        Map<String, SchedulerType> map = new HashMap<>();
        for (SchedulerType instance : SchedulerType.values()) {
            System.out.println("TYPE: " + instance.name());
            map.put(instance.name(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static SchedulerType get(String name) {
        return ENUM_MAP.getOrDefault(name, COMPUTATION);
    }

}

