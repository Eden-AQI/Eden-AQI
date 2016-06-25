package com.semc.aqi.config;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.semc.aqi.model.Grade;
import com.semc.aqi.model.GradeInfo;

public class BizUtils {

    public static int getGradleLevel(int num) {
        int result = 1;

        GradeInfo gradeInfo = Global.getGradeInfo();
        for (int i = 0; i < gradeInfo.size(); i++) {
            if (num <= gradeInfo.get(i).getAQIMax()) {
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
            if (num <= grade.getAQIMax()) {
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

    public static String getGradleText(int num) {
        String result = "优";

        GradeInfo gradeInfo = Global.getGradeInfo();
        for (int i = 0; i < gradeInfo.size(); i++) {
            Grade grade = gradeInfo.get(i);
            if (num <= grade.getAQIMax()) {
                result = grade.getAQIState().trim();
                break;
            } else {
                continue;
            }
        }
        return result;
    }

    public static int getGradleLevelByState(@NonNull String state) {
        int result = 1;

        if (state.contains("~")) {
            state = state.split("~")[1];
        }

        GradeInfo gradeInfo = Global.getGradeInfo();
        for (int i = 0; i < gradeInfo.size(); i++) {
            Grade grade = gradeInfo.get(i);
            if (grade.getAQIState().trim().equals(state.trim())) {
                result = grade.getGrade();
                break;
            } else {
                continue;
            }
        }
        return result;
    }
}
