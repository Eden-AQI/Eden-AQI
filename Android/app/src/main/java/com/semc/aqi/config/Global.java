package com.semc.aqi.config;

import android.content.Context;
import android.text.TextUtils;

import com.jayfeng.lesscode.core.SharedPreferenceLess;

public class Global {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }
    public static void setContext(Context context) {
        sContext = context;
    }
}