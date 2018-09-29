package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import com.google.gson.annotations.SerializedName;

public class Url {
    @SerializedName("url")
    private String url;

    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
