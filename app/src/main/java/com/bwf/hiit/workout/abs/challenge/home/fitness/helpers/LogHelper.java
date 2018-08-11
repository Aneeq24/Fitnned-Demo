package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;


public class LogHelper {

    public static void logD(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

}
