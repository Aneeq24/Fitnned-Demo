package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class DayProgressModel  implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("progress")
    private int progress;

    @SerializedName("complete")
    private boolean complete;



    public DayProgressModel(int id, int progress, boolean complete) {
        this.id = id;
        this.progress = progress;
        this.complete = complete;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

