package com.example.laspiedrasapp.models;

public class ReviewCommerceModel {
    private String review;
    private String userId;
    private String stars;

    public ReviewCommerceModel() {
    }


    public ReviewCommerceModel(String reviw, String userId, String stars) {
        this.review = reviw;
        this.userId = userId;
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String reviw) {
        this.review = reviw;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}
