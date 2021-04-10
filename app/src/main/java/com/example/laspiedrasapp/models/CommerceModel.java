package com.example.laspiedrasapp.models;

public class CommerceModel {
    private String name;
    private String description;
    private String banner_url;

    public CommerceModel(){

    }

    public CommerceModel(String name, String description, String banner_url) {
        this.name = name;
        this.description = description;
        this.banner_url = banner_url;
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

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }
}
