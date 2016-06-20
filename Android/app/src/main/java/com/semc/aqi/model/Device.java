package com.semc.aqi.model;

public class Device {

    public static final int DEVICE_TYPE_PHONE = 1;
    public static final int DEVICE_TYPE_TABLET = 2;

    private String DeviceNumber;
    private int Platform = 2;
    private int DeviceType;
    private String PushId;
    private double Latitude;
    private double Longitude;

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    public int getPlatform() {
        return Platform;
    }

    public void setPlatform(int platform) {
        Platform = platform;
    }

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int deviceType) {
        DeviceType = deviceType;
    }

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}