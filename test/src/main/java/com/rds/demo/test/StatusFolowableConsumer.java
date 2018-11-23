package com.rds.demo.test;

import com.rds.demo.api.FlowableProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class StatusFolowableConsumer {

    @Reference
    private FlowableProvider<String> status;

    @Activate
    public void activate() {
        status.subscribe(System.out::println);
    }
}
