package com.bwf.hiit.workout.abs.challenge.home.fitness.models;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Plan implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("lang")
    private int lang;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @Ignore
    private int progress;

    public Plan(int id, int lang, String name, String image) {
        this.id = id;
        this.lang = lang;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
