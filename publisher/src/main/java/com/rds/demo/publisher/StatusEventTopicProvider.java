package com.rds.demo.publisher;

import com.rds.demo.api.EventTopic;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = EventTopic.class,
        immediate = true
)
public class StatusEventTopicProvider implements EventTopic<String> {

    private final PublishSubject<String> status;

    public StatusEventTopicProvider() {
        status = PublishSubject.create();
    }

    @Activate
    public void activate() {
        System.out.println("ACTIVATE");
    }

    @Deactivate
    public void deactivate() {
        System.out.println("DEACTIVATE");
    }

    @Override
    public void publish(String event) {
        status.onNext(event);
    }

    @Override
    public Observable<String> stream() {
        return status;
    }
}
