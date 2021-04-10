package com.example.laspiedrasapp.models;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.sql.Timestamp;

public class ProfileModel {
    private String name;
    private String phone;
    private String imgUrl;

    public  ProfileModel(){

    }

    public ProfileModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

//   https://youtu.be/765aoufNc8c?t=295
}
