package com.semc.aqi.model;

public class ForecastItem {
    private String Time;
    private String AqiLevel;
    private String Aqi;
    private String PrimaryParameter;
    private String Weather;
    private String Temperature;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAqiLevel() {
        return AqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        AqiLevel = aqiLevel;
    }

    public String getAqi() {
        return Aqi;
    }

    public void setAqi(String aqi) {
        Aqi = aqi;
    }

    public String getPrimaryParameter() {
        return PrimaryParameter;
    }

    public void setPrimaryParameter(String primaryParameter) {
        PrimaryParameter = primaryParameter;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }
}
