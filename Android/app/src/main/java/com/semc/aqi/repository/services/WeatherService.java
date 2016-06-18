package com.semc.aqi.repository.services;

import com.semc.aqi.model.RealTime;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {
    @GET("GetRealtime")
    Observable<RealTime> getRealTime(@Query("siteId") String siteId);
}
