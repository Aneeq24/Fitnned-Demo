package com.bwf.hiit.workout.abs.challenge.home.fitness.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @PrimaryKey
    private int id;

    private int gender;

    private int age;

    private float height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int  getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
