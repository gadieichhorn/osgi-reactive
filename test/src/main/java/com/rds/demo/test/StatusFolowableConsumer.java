package com.rds.demo.test;

import com.rds.demo.api.FlowableProvider;
import io.reactivex.disposables.Disposable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class StatusFolowableConsumer {

    Disposable disposable;

    @Reference
    private FlowableProvider<String> status;

    @Activate
    public void activate() {
        disposable = status.subscribe(System.out::println);
    }

    @Deactivate
    public void deactivate() {
        disposable.dispose();
    }

}
