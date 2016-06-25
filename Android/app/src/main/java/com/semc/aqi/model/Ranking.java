package com.semc.aqi.model;

import java.util.List;

public class Ranking {
    private String Duration;
    private List<RankingItem> Items;

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public List<RankingItem> getItems() {
        return Items;
    }

    public void setItems(List<RankingItem> items) {
        Items = items;
    }
}
