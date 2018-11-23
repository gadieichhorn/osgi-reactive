package com.rds.demo.publisher;

import com.rds.demo.api.FlowableProvider;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.concurrent.TimeUnit;

@Component(
        service = FlowableProvider.class,
        property = "stream.id=status.flow.provider"

)
public class StatusFolowableProvider implements FlowableProvider<String> {

    private final Flowable<String> source;

    public StatusFolowableProvider() {
        this.source = Flowable.interval(5000, 1000, TimeUnit.MILLISECONDS)
                .map(aLong -> String.valueOf(aLong));
    }

    @Activate
    public void activate() {
        // TODO
    }

    public Disposable subscribe(Consumer<String> onNext) {
        return source.subscribe(onNext);
    }

}
