package com.semc.aqi.repository.services;

import com.semc.aqi.model.AppUpdate;

import retrofit2.http.GET;
import rx.Observable;

public interface ConfigService {

    @GET("muai/latest.json")
    Observable<AppUpdate> checkAppUpdate();
}
