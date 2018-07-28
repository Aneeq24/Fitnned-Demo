package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import java.util.ArrayList;

public class DataModelWorkout
{
    //region [30 Day Data Info]
    public String[] dayName;
    public  ArrayList<Float> progress = new ArrayList<Float>();
    public int[] iconForExcersice;
    public int  curretPlan;
    //endregion

    //region [Playing Animation]

        public boolean hasAnimation;
        public int totalTimeForExcersice = 0;
        public int skipTime = 1500;
        public int[] animationImages = null;
        public int stillImage = 0;
        public String exerciseName;
        public int animationRelaceImageTime = 300;

    //endregion


    //region [Daily ExerciseList]
        public ArrayList<String> dailyExercise_ExerciseName = new ArrayList<String>();
        public ArrayList<String> dailyExercise_VideoView = new ArrayList<String>();
        public  ArrayList<Integer> dailyExercise_ImageIndex = new ArrayList<Integer>();
    //endregion


    public DataModelWorkout()
    {
        dayName = new String[]{};
        progress = new ArrayList<Float>();
        iconForExcersice = null;

    }
}
