package com.semc.aqi;

import android.app.Application;

import com.jayfeng.lesscode.core.$;
import com.semc.aqi.config.Global;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;

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
            City city = new City("上海", 21);
            LiteOrmManager.getLiteOrm(this).save(city);
        }

    }
}