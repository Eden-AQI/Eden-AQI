package com.semc.aqi.event;

public class CurrentCityEvent {
    private int index;

    public CurrentCityEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
