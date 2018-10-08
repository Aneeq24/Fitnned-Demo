package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Exercise {

    @PrimaryKey
    private int id;

    private int unit;

    private int lang;

    @SerializedName("calories")
    private float calories;

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("tts")
    @TypeConverters(ConvertersTts.class)
    private List<Tts> tts;

    @SerializedName("display")
    private String display;

    @Ignore
    Exercise(){
    }

    @Ignore
    public Exercise(int unit, int lang, String name, String display_name) {
        this.unit = unit;
        this.lang = lang;
        this.name = name;
        this.display = display_name;
    }

    public Exercise(int id, int unit, int lang, float calories, String name, String url, List<Tts> tts, String display) {
        this.id = id;
        this.unit = unit;
        this.lang = lang;
        this.calories = calories;
        this.name = name;
        this.url = url;
        this.tts = tts;
        this.display = display;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Tts> getTts() {
        return tts;
    }

    public void setTts(List<Tts> tts) {
        this.tts = tts;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
