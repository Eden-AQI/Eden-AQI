package com.semc.aqi.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Id : 253
 * Name : 杨浦新江湾城
 * Aqi : 99
 * BackgroundImageUrl : null
 * Group : Y
 * Latitude : 31.32146
 * Longitude : 121.5034
 * Grade : 2
 */

@Table("city")
public class City {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    // 取名为“_id”,如果此处不重新命名,就采用属性名称
    @Column("_id")
    public int _id;

    @Column("city_id")
    private int Id;
    private String Name;
    private int Aqi;
    private String BackgroundImageUrl;
    @Column("group_name")
    private String Group;
    private double Latitude;
    private double Longitude;
    private int Grade;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getAqi() {
        return Aqi;
    }

    public void setAqi(int Aqi) {
        this.Aqi = Aqi;
    }

    public String getBackgroundImageUrl() {
        return BackgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        BackgroundImageUrl = backgroundImageUrl;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int Grade) {
        this.Grade = Grade;
    }
}
