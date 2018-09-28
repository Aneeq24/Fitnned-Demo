package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import com.google.gson.annotations.SerializedName;

public class Tts {
    @SerializedName("time")
    private String time;
    @SerializedName("text")
    private String text;

    public Tts() {
    }

    public Tts(String time, String text) {
        this.time = time;
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
}
