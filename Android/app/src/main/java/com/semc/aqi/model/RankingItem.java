package com.semc.aqi.model;

import java.util.List;

public class RankingItem {
    private String Parameter;
    private List<RankingItemData> Data;

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public List<RankingItemData> getData() {
        return Data;
    }

    public void setData(List<RankingItemData> data) {
        Data = data;
    }
}
