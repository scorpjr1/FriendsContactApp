package com.example.shiyan1.model.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "room_baseinfo")
public class Haoyou {
    @PrimaryKey(autoGenerate = true)
    public int infoid = 0;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "hobby")
    public String hobby;

    @ColumnInfo(name = "place")
    public String place;

    @ColumnInfo(name = "majorid")
    public int majorid;

    @ColumnInfo(name = "focus")
    public Boolean focus = false;

    @ColumnInfo(name = "profile")
    public String profile;


    public Haoyou(@NonNull String name, @NonNull String phone, String sex, String hobby, String place, int majorid, Boolean focus, String profile) {
        this.name = name;
        this.phone = phone;
        this.sex = sex;
        this.place = place;
        this.hobby = hobby;
        this.majorid = majorid;
        this. focus = focus;
        this.profile = profile;
    }

    @Ignore
    public Haoyou(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

    public Boolean getFocus() {
        return focus;
    }

    public void setFocus(Boolean focus) {
        this.focus = focus;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
