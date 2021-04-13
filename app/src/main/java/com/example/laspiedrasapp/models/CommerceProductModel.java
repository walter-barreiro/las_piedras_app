package com.example.laspiedrasapp.models;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class CommerceProductModel implements Serializable {
    private String id;
    private String userId;
    private String name;
    private String price;
    private String description;
    private String imgUrl;

    public CommerceProductModel() {

    }

    public CommerceProductModel(String id, String userId, String name, String description, String imgUrl) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
