package com.example.laspiedrasapp.models;

import java.io.Serializable;

public class CategoriaModel implements Serializable {

    public CategoriaModel() {
    }

    private String id;
    private String image;
    private String title;
    private String category;


    public CategoriaModel(String id, String image, String title, String category) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
