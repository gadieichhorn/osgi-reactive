package com.rds.demo.consumer;

import com.rds.demo.api.FlowableProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class ConsumerProvider {

    @Reference(target = "(stream.id=status.flow.provider)")
    private FlowableProvider<String> status;

    @Activate
    public void activate() {
        status.subscribe(System.out::println);
    }

}
