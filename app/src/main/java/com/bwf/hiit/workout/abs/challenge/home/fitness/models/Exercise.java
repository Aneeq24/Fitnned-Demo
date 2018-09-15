package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Exercise {

    @PrimaryKey
    private int id;

    private int unit;

    private int lang;

    private float calories;

    private String name;

    private String tts;

    @SerializedName("display")
    private String display_name;

    @Ignore
    public Exercise(int unit, int lang, String name, String display_name) {
        this.unit = unit;
        this.lang = lang;
        this.name = name;
        this.display_name = display_name;
    }

    public Exercise(int id, int unit, int lang, float calories, String name, String tts, String display_name) {
        this.id = id;
        this.unit = unit;
        this.lang = lang;
        this.calories = calories;
        this.name = name;
        this.tts = tts;
        this.display_name = display_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
}
