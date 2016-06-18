package com.semc.aqi.repository;

import com.google.gson.reflect.TypeToken;
import com.semc.aqi.model.IpInfo;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.repository.config.RetrofitManager;
import com.semc.aqi.repository.config.RxRetrofitCache;
import com.semc.aqi.repository.json.Result;
import com.semc.aqi.repository.services.WeatherService;

import rx.Observable;

public class WeatherRepository {

    private volatile static WeatherRepository instance;

    private WeatherRepository() {
    }

    public static WeatherRepository getInstance() {
        if (instance == null) {
            synchronized (WeatherRepository.class) {
                if (instance == null) {
                    instance = new WeatherRepository();
                }
            }
        }
        return instance;
    }

    public Observable<RealTime> getRealTime(String siteId, boolean forceRefresh) {

        final long expireTime = 600000;
        final String cacheKey = "realtime_" + siteId;

        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<RealTime> fromNetwork = weatherService.getRealTime(siteId);

        return RxRetrofitCache.load(cacheKey, expireTime, fromNetwork, forceRefresh, new TypeToken<RealTime>() {
        }.getType());
    }
}