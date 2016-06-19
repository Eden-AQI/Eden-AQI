package com.semc.aqi.config;

import android.graphics.Color;

import com.semc.aqi.model.Grade;
import com.semc.aqi.model.GradeInfo;

public class BizUtils {

    public static int getGradleLevel(int num) {
        int result = 1;

        GradeInfo gradeInfo = Global.getGradeInfo();
        for (int i = 0; i < gradeInfo.size(); i++) {
            if (num < gradeInfo.get(i).getAQIMax()) {
                result = i + 1;
                break;
            } else {
                continue;
            }
        }
        return result;
    }

    public static int getGradleColor(int num) {
        int result = Color.rgb(0, 228, 0);

        GradeInfo gradeInfo = Global.getGradeInfo();
        for (int i = 0; i < gradeInfo.size(); i++) {
            Grade grade = gradeInfo.get(i);
            if (num < grade.getAQIMax()) {
                result = Color.rgb(grade.getColorR(),
                        grade.getColorG(),
                        grade.getColorB());
                break;
            } else {
                continue;
            }
        }
        return result;
    }
}
