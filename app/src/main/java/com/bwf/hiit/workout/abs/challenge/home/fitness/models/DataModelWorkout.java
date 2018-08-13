package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import java.util.ArrayList;

public class DataModelWorkout {
    //region [30 Day Data Info]
    public String[] dayName;
    public ArrayList<Float> progress;
    public int[] iconForExcersice;
    public int curretPlan;
    //endregion

    public ArrayList<String> dailyExercise_ExerciseName = new ArrayList<>();
    public ArrayList<String> dailyExercise_VideoView = new ArrayList<>();
    public ArrayList<Integer> exercisTimeList = new ArrayList<>();
    public ArrayList<Integer> resetTimeList = new ArrayList<>();

    //endregion


    public DataModelWorkout() {
        dayName = new String[]{};
        progress = new ArrayList<>();
        iconForExcersice = null;

    }
}
