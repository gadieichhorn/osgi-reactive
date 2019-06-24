package com.rds.demo.provider;

import com.rds.demo.api.Message;
import com.rds.demo.api.MessageBus;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = MessageBus.class, scope = ServiceScope.SINGLETON)
public class MessageBusProvider<T extends Message> implements MessageBus<T> {

    private final PublishSubject<T> publishSubject = PublishSubject.create();

    @Override
    public void publish(T message) {
        publishSubject.onNext(message);
    }

    @Override
    public void subscribe(Observer<T> observer) {
        publishSubject.subscribe(observer);
    }

}
