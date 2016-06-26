package com.semc.aqi.model;

public class Update {
    private int VersionCode;
    private String VersionName;
    private String Description;
    private String DownloadUrl;
    private boolean Mandatory;

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }

    public boolean isMandatory() {
        return Mandatory;
    }

    public void setMandatory(boolean mandatory) {
        Mandatory = mandatory;
    }
}
