package com.rds.demo.consumer;

import com.rds.demo.api.EventTopic;
import io.reactivex.disposables.Disposable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true
)
public class StatusConsumerProvider {

    private Disposable disposable;

    @Reference
    private EventTopic<String> status;

    @Activate
    public void activate() {
        System.out.println("ACTIVATE");
        disposable = status.stream().subscribe(System.out::println);
    }

    @Deactivate
    public void deactivate() {
        System.out.println("DEACTIVATE");
        disposable.dispose();
    }

}
