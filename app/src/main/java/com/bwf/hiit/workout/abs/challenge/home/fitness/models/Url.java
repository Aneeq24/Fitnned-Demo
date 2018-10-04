package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import com.google.gson.annotations.SerializedName;

public class Url {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String image;

    public Url(String title, String url, String image) {
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
