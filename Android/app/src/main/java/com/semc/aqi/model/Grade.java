package com.semc.aqi.model;

/**
 * Id : 1
 * GID : 1
 * Grade : 1
 * GradeName : 一级
 * AQIMin : 0
 * AQIMax : 50
 * AQIState : 优
 * ColorR : 0
 * ColorG : 228
 * ColorB : 0
 * HealthEffect : 空气质量令人满意，基本无空气污染
 * Method : 各类人群可正常活动
 */
public class Grade {
    private int Id;
    private int GID;
    private int Grade;
    private String GradeName;
    private int AQIMin;
    private int AQIMax;
    private String AQIState;
    private int ColorR;
    private int ColorG;
    private int ColorB;
    private String HealthEffect;
    private String Method;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int Grade) {
        this.Grade = Grade;
    }

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String GradeName) {
        this.GradeName = GradeName;
    }

    public int getAQIMin() {
        return AQIMin;
    }

    public void setAQIMin(int AQIMin) {
        this.AQIMin = AQIMin;
    }

    public int getAQIMax() {
        return AQIMax;
    }

    public void setAQIMax(int AQIMax) {
        this.AQIMax = AQIMax;
    }

    public String getAQIState() {
        return AQIState;
    }

    public void setAQIState(String AQIState) {
        this.AQIState = AQIState;
    }

    public int getColorR() {
        return ColorR;
    }

    public void setColorR(int ColorR) {
        this.ColorR = ColorR;
    }

    public int getColorG() {
        return ColorG;
    }

    public void setColorG(int ColorG) {
        this.ColorG = ColorG;
    }

    public int getColorB() {
        return ColorB;
    }

    public void setColorB(int ColorB) {
        this.ColorB = ColorB;
    }

    public String getHealthEffect() {
        return HealthEffect;
    }

    public void setHealthEffect(String HealthEffect) {
        this.HealthEffect = HealthEffect;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String Method) {
        this.Method = Method;
    }
}
