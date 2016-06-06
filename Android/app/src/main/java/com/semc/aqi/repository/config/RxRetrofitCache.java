package com.semc.aqi.repository.config;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jayfeng.lesscode.core.CacheLess;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxRetrofitCache {
    public static <T> Observable<T> load(final String cacheKey, final long expireTime, Observable<T> fromNetwork, boolean forceRefresh, final Type clazz) {

        Observable<T> fromCache = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                String cache = CacheLess.$get(cacheKey, expireTime);
                if (!TextUtils.isEmpty(cache)) {
                    T result = new Gson().fromJson(cache, clazz);
                    subscriber.onNext(result);
                } else {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io());

        fromNetwork = fromNetwork.subscribeOn(Schedulers.newThread())
                .map(new Func1<T, T>() {
                    @Override
                    public T call(T mapResult) {
                        String cache = new Gson().toJson(mapResult);
                        CacheLess.$set(cacheKey, cache);
                        return mapResult;
                    }
                });

        if (forceRefresh) {
            return fromNetwork;
        } else {
            return Observable.concat(fromCache, fromNetwork).first();
        }
    }
}
