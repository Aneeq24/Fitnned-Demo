package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Url {

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    @TypeConverters(ConvertersContent.class)
    private List<Content> content;

    public Url(String title, List<Content> content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }
}
