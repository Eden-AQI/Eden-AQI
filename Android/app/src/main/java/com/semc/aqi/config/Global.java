package com.semc.aqi.config;

import android.content.Context;

import com.google.gson.Gson;
import com.jayfeng.lesscode.core.FileLess;
import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.model.GradeInfo;

import java.util.ArrayList;

public class Global {

    private static Context context;
    private static GradeInfo gradeInfo;

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
}
