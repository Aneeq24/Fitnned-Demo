package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;


public class ToastHelper {

    public static void makeToast(String text) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(Application.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
