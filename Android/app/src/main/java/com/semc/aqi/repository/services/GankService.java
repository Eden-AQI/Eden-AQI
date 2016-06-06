package com.semc.aqi.repository.services;

import com.semc.aqi.model.GankPic;
import com.semc.aqi.repository.json.GankListResult;

import retrofit2.http.GET;
import rx.Observable;

public interface GankService {
    @GET("api/data/福利/10/1")
    Observable<GankListResult<GankPic>> getGankList();
}
