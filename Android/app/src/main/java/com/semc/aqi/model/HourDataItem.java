package com.semc.aqi.model;

public class HourDataItem {
    private String Time;
    private float Value;
    private int Aqi;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public float getValue() {
        return Value;
    }

    public void setValue(float value) {
        Value = value;
    }

    public int getAqi() {
        return Aqi;
    }

    public void setAqi(int aqi) {
        Aqi = aqi;
    }
}
