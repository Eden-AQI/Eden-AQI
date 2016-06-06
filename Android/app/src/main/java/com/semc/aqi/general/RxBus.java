package com.semc.aqi.general;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * 基于Rxjava的事件总线框架
 */
public class RxBus {

    private static volatile RxBus mInstance;
    private final SerializedSubject<Object, Object> mSubject;

    private RxBus() {
        mSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void post(Object object) {
        mSubject.onNext(object);
    }

    public <T> Observable<T> toObservable(final Class<T> type) {
        return mSubject.ofType(type);
    }

    public boolean hasObservers() {
        return mSubject.hasObservers();
    }
}
