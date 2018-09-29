package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Food {

    @PrimaryKey
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("tts")
    @TypeConverters(ConvertersUrl.class)
    private List<Url> foodDetail;

    public Food(int id, String name, List<Url> foodDetail) {
        this.id = id;
        this.name = name;
        this.foodDetail = foodDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Url> getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(List<Url> foodDetail) {
        this.foodDetail = foodDetail;
    }
}
