package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.content.Context;

public class AppStateManager {

    public static int mainCategory = 0;

    public static int currentExercise = 0;
    public static int roundCleared = 0;

    private static AppStateManager instance;

    public static void getInstance(Context context) {
        if (instance == null) {
            instance = new AppStateManager();
        }
    }

    private AppStateManager() {

    }

}
