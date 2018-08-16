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

    private int height;

    private int weight;

    private int totalExcercise;

    private int totalKcal;

    private int totalTime;

    private int bmi;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getTotalExcercise() {
        return totalExcercise;
    }

    public void setTotalExcercise(int totalExcercise) {
        this.totalExcercise = totalExcercise;
    }

    public int getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(int totalKcal) {
        this.totalKcal = totalKcal;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }
}
