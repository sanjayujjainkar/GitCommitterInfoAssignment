package com.pof.gitcommitterinfo.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class Utility {

    /*public static Observable<Integer> getPeriodicObservable() {

        return Observable.interval(2, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long it) throws Exception {
                        return (int)(long)(it%2);
                    }
                });

    }*/

    public static Flowable<Integer> getPeriodicObservable() {
        PublishSubject<Integer> publisher = PublishSubject.create();

        return Observable.interval(2, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long it) throws Exception {
                        return (int)(long)(it%2);
                    }
                }).toFlowable(BackpressureStrategy.LATEST);
    }

}
