package com.semc.aqi.model;

import java.util.List;

public class HourItem {
    private String Parameter;
    private List<HourDataItem> Data;

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public List<HourDataItem> getData() {
        return Data;
    }

    public void setData(List<HourDataItem> data) {
        Data = data;
    }
}
