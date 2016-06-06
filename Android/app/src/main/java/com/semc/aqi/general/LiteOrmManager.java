package com.semc.aqi.general;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

public class LiteOrmManager {
    private volatile static LiteOrm instance;

    private LiteOrmManager() {
    }

    public static LiteOrm getLiteOrm(Context context) {
        if (instance == null) {
            synchronized (LiteOrmManager.class) {
                if (instance == null) {
                    instance = LiteOrm.newSingleInstance(context.getApplicationContext(), "weather.db");
                }
            }
        }
        return instance;
    }
}
