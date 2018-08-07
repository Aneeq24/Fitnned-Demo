package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.content.Context;
import android.content.SharedPreferences;

public class AppStateManager {

    public  static int mainCategory = 0;
    public  static  int dayPlanSelected = 0;

    public  static  int sevenMinCurrentIndex = 0;

    public  static  int dailyExercise_ExeciseIndex = 0;

    public  static int currentExercise = 0;
    public  static  int roundCleared = 0;

    private static  AppStateManager instance;

    public static void Instance(Context context) {
            if (instance == null)
            {
                instance = new AppStateManager(context);
            }
    }

    private AppStateManager(Context context) {
        String prefName = "pref_Name";
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

}
