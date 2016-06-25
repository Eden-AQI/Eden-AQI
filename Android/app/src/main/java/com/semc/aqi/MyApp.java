package com.semc.aqi;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.jayfeng.lesscode.core.$;
import com.semc.aqi.config.Constant;
import com.semc.aqi.config.Global;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;

import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Global.setContext(this);

        $.getInstance()
                .context(getApplicationContext())
                .log(true, "feng")
                .build();

        LiteOrmManager.getLiteOrm(this);

        if (LiteOrmManager.getLiteOrm(this).queryCount(City.class) == 0) {
            City city = new City();
            city.setName("郑州市");
            city.setId(0);
            city.setGroup("Z");
            city.setLatitude(Constant.ZZ_LAT);
            city.setLongitude(Constant.ZZ_LNG);
            LiteOrmManager.getLiteOrm(this).save(city);
        }

        SDKInitializer.initialize(getApplicationContext());

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
