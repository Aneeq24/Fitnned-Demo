package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import java.util.ArrayList;

public class DataModelWorkout {

    public String[] dayName;
    public ArrayList<Float> progress;
    public int[] iconForExcersice;
    public int curretPlan;

    public DataModelWorkout() {
        dayName = new String[]{};
        progress = new ArrayList<>();
        iconForExcersice = null;
    }
}
