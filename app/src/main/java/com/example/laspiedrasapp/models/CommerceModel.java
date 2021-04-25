package com.example.laspiedrasapp.models;

import java.io.Serializable;

public class CommerceModel implements Serializable {
    private String name;
    private String description;
    private String banner_url;
    private String ownerId;

    public CommerceModel(){

    }

    public CommerceModel(String name, String description, String banner_url, String ownerId) {
        this.name = name;
        this.description = description;
        this.banner_url = banner_url;
        this.ownerId = ownerId;
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

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

}
