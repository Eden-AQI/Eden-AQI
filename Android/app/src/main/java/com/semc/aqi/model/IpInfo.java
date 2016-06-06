package com.semc.aqi.model;

import android.support.annotation.Keep;

/**
 * {
 * "code": 0,
 * "data": {
 * "country": "美国",
 * "country_id": "US",
 * "area": "",
 * "area_id": "",
 * "region": "",
 * "region_id": "",
 * "city": "",
 * "city_id": "",
 * "county": "",
 * "county_id": "",
 * "isp": "",
 * "isp_id": "",
 * "ip": "21.22.11.33"
 * }
 * }
 */

@Keep
public class IpInfo {
    private String country;
    private String country_id;
    private String ip;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
