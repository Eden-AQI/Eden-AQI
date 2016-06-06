package com.semc.aqi.model;

import android.support.annotation.Keep;

@Keep
public class GankPic {

    private String _id;
    private String url;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
