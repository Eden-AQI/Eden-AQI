package com.semc.aqi.model;

public class HourDataItem {
    private int Time;
    private int Value;
    private int Aqi;

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public int getAqi() {
        return Aqi;
    }

    public void setAqi(int aqi) {
        Aqi = aqi;
    }
}
