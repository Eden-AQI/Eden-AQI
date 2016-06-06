package com.semc.aqi.repository;

import com.google.gson.reflect.TypeToken;
import com.semc.aqi.model.IpInfo;
import com.semc.aqi.repository.config.RetrofitManager;
import com.semc.aqi.repository.config.RxRetrofitCache;
import com.semc.aqi.repository.json.Result;
import com.semc.aqi.repository.services.IpService;

import rx.Observable;

public class ConfigRepository {

    private volatile static ConfigRepository instance;

    private ConfigRepository() {
    }

    public static ConfigRepository getInstance() {
        if (instance == null) {
            synchronized (ConfigRepository.class) {
                if (instance == null) {
                    instance = new ConfigRepository();
                }
            }
        }
        return instance;
    }
}
