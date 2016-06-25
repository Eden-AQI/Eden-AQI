package com.semc.aqi.repository;

import com.google.gson.reflect.TypeToken;
import com.semc.aqi.model.Device;
import com.semc.aqi.model.IpInfo;
import com.semc.aqi.model.Ranking;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.model.Update;
import com.semc.aqi.repository.config.RetrofitManager;
import com.semc.aqi.repository.config.RxRetrofitCache;
import com.semc.aqi.repository.json.Result;
import com.semc.aqi.repository.services.WeatherService;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    /**
     * 获取数据
     *
     * @param siteId
     * @param forceRefresh
     * @return
     */
    public Observable<RealTime> getRealTime(String siteId, boolean forceRefresh) {

        final long expireTime = 600000;
        final String cacheKey = "realtime_" + siteId;

        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<RealTime> fromNetwork = weatherService.getRealTime(siteId);

        return RxRetrofitCache.load(cacheKey, expireTime, fromNetwork, forceRefresh, new TypeToken<RealTime>() {
        }.getType());
    }

    /**
     * 检查更新
     *
     * @return
     */
    public Observable<Update> checkUpdate() {
        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<Update> result = weatherService.checkUpdate();

        return result;
    }

    /**
     * 注册设备
     *
     * @param device
     * @return
     */
    public Observable<String> registerDevice(Device device) {
        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<String> result = weatherService.registerDevice(device);

        return result;
    }

    /**
     *  heart beat
     *
     * @param deviceNumber
     * @return
     */
    public Observable<String> heartbeat(@Query("deviceNumber") String deviceNumber) {
        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<String> result = weatherService.heartbeat(deviceNumber);

        return result;
    }


    public Observable<List<Ranking>> getRankingData() {
        WeatherService weatherService = RetrofitManager.getRxRetrofit().create(WeatherService.class);
        Observable<List<Ranking>> result = weatherService.getRankingData();

        return result;
    }
}