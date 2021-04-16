package com.example.laspiedrasapp.models;

import java.io.Serializable;
import java.util.Date;

public class BusinessModel implements Serializable {
    private String id;
    private String profession;
    private String imgUrl;
    private String description;
//    private String location;
//    private Date createdAt;
//    private Date updateAt;

    public BusinessModel(){

    }

    public BusinessModel(String id, String profession, String imgUrl, String description) {
        this.id = id;
        this.profession = profession;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
