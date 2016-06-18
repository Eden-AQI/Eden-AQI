package com.semc.aqi.model;

import java.util.List;

public class RealTime {
    private String Name;
    private String Background;
    private String UpdateTime;
    private int Aqi;
    private String AqiLevel;
    private String PrimaryParameter;
    private String PrimaryValue;
    private String Health;
    private String Suggest;
    private List<ForecastItem> Forecast;
    private List<OtherParameterItem> OtherParameters;
    private List<HourItem> Hours;
    private List<DaysItem> Days;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBackground() {
        return Background;
    }

    public void setBackground(String background) {
        Background = background;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getAqi() {
        return Aqi;
    }

    public void setAqi(int aqi) {
        Aqi = aqi;
    }

    public String getAqiLevel() {
        return AqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        AqiLevel = aqiLevel;
    }

    public String getPrimaryParameter() {
        return PrimaryParameter;
    }

    public void setPrimaryParameter(String primaryParameter) {
        PrimaryParameter = primaryParameter;
    }

    public String getPrimaryValue() {
        return PrimaryValue;
    }

    public void setPrimaryValue(String primaryValue) {
        PrimaryValue = primaryValue;
    }

    public String getHealth() {
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getSuggest() {
        return Suggest;
    }

    public void setSuggest(String suggest) {
        Suggest = suggest;
    }

    public List<ForecastItem> getForecast() {
        return Forecast;
    }

    public void setForecast(List<ForecastItem> forecast) {
        Forecast = forecast;
    }

    public List<OtherParameterItem> getOtherParameters() {
        return OtherParameters;
    }

    public void setOtherParameters(List<OtherParameterItem> otherParameters) {
        OtherParameters = otherParameters;
    }

    public List<HourItem> getHours() {
        return Hours;
    }

    public void setHours(List<HourItem> hours) {
        Hours = hours;
    }

    public List<DaysItem> getDays() {
        return Days;
    }

    public void setDays(List<DaysItem> days) {
        Days = days;
    }
}
