package com.semc.aqi.config;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jayfeng.lesscode.core.FileLess;
import com.jayfeng.lesscode.core.SharedPreferenceLess;
import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.model.GradeInfo;

import java.util.ArrayList;

public class Global {

    private static Context context;
    private static GradeInfo gradeInfo;
    private static String deviceNumber;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Global.context = context;
    }

    public static GradeInfo getGradeInfo() {
        if (gradeInfo == null) {
            try {
                gradeInfo = new Gson().fromJson(
                        FileLess.$read(context.getResources().getAssets().open("grade_info.json")), GradeInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gradeInfo;
    }

    public static String getDeviceNumber() {
        if (TextUtils.isEmpty(deviceNumber)) {
            deviceNumber = SharedPreferenceLess.$get(context, Constant.PREFERENCE_KEY_DEVICE_NUMBER, "");
        }
        return deviceNumber;
    }

    public static void setDeviceNumber(String deviceNumber) {
        SharedPreferenceLess.$put(context, Constant.PREFERENCE_KEY_DEVICE_NUMBER, deviceNumber);
        Global.deviceNumber = deviceNumber;
    }
}
