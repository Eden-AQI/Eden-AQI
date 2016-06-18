package com.semc.aqi.repository.config;

import com.semc.aqi.config.Global;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientManager {

    /**
     * *******************************************************************
     * singleton
     * *******************************************************************
     */
    private OkHttpClientManager() {
    }

    private static class SingletonHolder {
        private static final OkHttpClient instance = createOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return SingletonHolder.instance;
    }

    private static OkHttpClient createOkHttpClient() {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(headerInterceptor)
                .cache(getOkHttpCache())
                .build();
        return okHttpClient;
    }


    /**
     * global http header setting
     */
    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder newRequestBuilder = originalRequest.newBuilder();
            return chain.proceed(newRequestBuilder.build());
        }
    };

    private static Cache getOkHttpCache() {
        File cacheFile = new File(Global.getContext().getCacheDir(), "OkCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        return cache;
    }

}
