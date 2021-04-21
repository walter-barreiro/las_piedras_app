package com.example.laspiedrasapp.models;

import java.io.Serializable;

public class ProfileProductModel implements Serializable {
    // Modelo de los produtos que estan en el perfil general del usuario
    private String product_id;
    private String product_userId;
    private String product_name;
    private String product_description;
    private String product_stock;
    private String product_category;
    private String product_price;
    private String product_image_url;

    public ProfileProductModel(){ }

    public ProfileProductModel(String product_name){
        this.product_name = product_name;
    }

    public ProfileProductModel(String product_id, String product_userId, String product_name, String product_description, String product_price, String product_stock, String product_category) {
        this.product_id = product_id;
        this.product_userId = product_userId;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_stock = product_stock;
        this.product_category = product_category;
    }

    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_userId() {
        return product_userId;
    }
    public void setProduct_userId(String product_userId) {
        this.product_userId = product_userId;
    }

    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }
    public void setProduct_description(String product_description) {this.product_description = product_description;}

    public String getProduct_price() {
        return product_price;
    }
    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }
    public void setProduct_image_url(String product_image_url) { this.product_image_url = product_image_url; }

    public String getProduct_stock() { return product_stock; }
    public void setProduct_stock(String product_stock) { this.product_stock = product_stock; }

    public String getProduct_category() { return product_category;}
    public void setProduct_category(String product_category) {this.product_category = product_category; }


}
