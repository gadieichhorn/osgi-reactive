package com.rds.demo.publisher;

import com.rds.demo.api.EventTopic;
import org.osgi.service.component.annotations.*;

import java.util.UUID;

@Component(
        immediate = true
)
public class StatusFolowableProvider { //implements FlowableProvider<String> {

    @Reference
    private EventTopic<String> status;

    @Activate
    public void activate() {
        System.out.println("ACTIVATE");
        status.publish(UUID.randomUUID().toString());
    }

    @Deactivate
    public void deactivate() {
        System.out.println("DEACTIVATE");
    }

}
