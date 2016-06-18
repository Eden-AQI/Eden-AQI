package com.semc.aqi.model;

import java.util.List;

public class CityGroup {
    private String GroupName;
    private List<City> Items;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public List<City> getItems() {
        return Items;
    }

    public void setItems(List<City> items) {
        Items = items;
    }
}
