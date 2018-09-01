package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import java.util.ArrayList;

public class DataModelWorkout {

    public int curretPlan;
    public String[] dayName;
    public ArrayList<Float> progress;

    public DataModelWorkout() {
        curretPlan = 0;
        dayName = new String[]{};
        progress = new ArrayList<>();
    }
}
