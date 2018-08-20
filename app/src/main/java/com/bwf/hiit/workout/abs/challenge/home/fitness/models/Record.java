package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Record {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int kcal;

    private int exDay;

    private int duration;

    private String day;

    private String date;

    private String type;

    public Record() {
        setDay(getCurrentDay());
        setDate(getCurrentDate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExDay() {
        return exDay;
    }

    public void setExDay(int exDay) {
        this.exDay = exDay;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd - MMM - yy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getCurrentDay() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
