package com.semc.aqi.repository.services;

import com.semc.aqi.model.IpInfo;
import com.semc.aqi.repository.json.Result;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface IpService {
    @GET("service/getIpInfo.php")
    Observable<Result<IpInfo>> getIpInfo(@Query("ip") String ip);
}
