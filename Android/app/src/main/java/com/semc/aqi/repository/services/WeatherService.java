package com.semc.aqi.repository.services;

import com.semc.aqi.model.RealTime;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {
    @GET("Data/Realtime")
    Observable<RealTime> getRealTime(@Query("cityId") String cityId);
}
