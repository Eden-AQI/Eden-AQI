package com.semc.aqi.repository.config;


import com.semc.aqi.config.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit.Builder buildBaseRetrofit() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(OkHttpClientManager.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create());
        return retrofitBuilder;
    }

    public static Retrofit getRetrofit() {
        return buildBaseRetrofit()
                .baseUrl(Api.sBaseUrl)
                .build();
    }

    public static Retrofit getRxRetrofit() {
        return buildBaseRetrofit()
                .baseUrl(Api.sBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRxRetrofit(String baseUrl) {
        return buildBaseRetrofit()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
