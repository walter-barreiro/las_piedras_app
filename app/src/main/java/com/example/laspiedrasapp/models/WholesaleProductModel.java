package com.example.laspiedrasapp.models;

import java.io.Serializable;

public class WholesaleProductModel implements Serializable {
    private String id;
    private String title;
    private String unitPrice;
    private String wholesalePrice;
    private String description;
    private String imgUrl;
    private String purchased; // cantidad de comprados
    private String minimumAmount; // cantidad minimia para que se compre al por mayor

    public WholesaleProductModel(){

    }

    public WholesaleProductModel(String id, String title, String unitPrice, String wholesalePrice, String description, String imgUrl, String purchased, String minimumAmount) {
        this.id = id;
        this.title = title;
        this.unitPrice = unitPrice;
        this.wholesalePrice = wholesalePrice;
        this.description = description;
        this.imgUrl = imgUrl;
        this.purchased = purchased;
        this.minimumAmount = minimumAmount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
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
