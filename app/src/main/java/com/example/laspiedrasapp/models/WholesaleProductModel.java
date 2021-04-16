package com.example.laspiedrasapp.models;

import java.io.Serializable;

public class WholesaleProductModel implements Serializable {
    private String id;
    private String title;
    private String price;
    private String description;
    private String imgUrl;
    private String purchased; // cantidad de comprados
    private String minimumAmount; // cantidad minimia para que se compre al por mayor

    public WholesaleProductModel(){

    }

    public WholesaleProductModel(String id, String title, String price, String description, String imgUrl, String purchased, String minimumAmount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.purchased = purchased;
        this.minimumAmount = minimumAmount;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}
