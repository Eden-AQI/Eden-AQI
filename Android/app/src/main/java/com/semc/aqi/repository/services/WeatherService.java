package com.semc.aqi.repository.services;

import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.model.Device;
import com.semc.aqi.model.Ranking;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.model.Update;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {
    @GET("Aqi/GetRealtime")
    Observable<RealTime> getRealTime(@Query("siteId") String siteId);

    @GET("Metadata/Version")
    Observable<Update> checkUpdate();

    @POST("Device/RegisterDevice")
    Observable<String> registerDevice(@Body Device device);

    @POST("Device/Heartbeat")
    Observable<String> heartbeat(@Query("deviceNumber") String deviceNumber);

    @GET("Aqi/GetRankingData")
    Observable<List<Ranking>> getRankingData();

    @GET("Aqi/GetCityList")
    Observable<CityGroupList> getCityList();
}
