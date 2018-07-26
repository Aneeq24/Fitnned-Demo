package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.os.Bundle;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.AppConstants;
import com.google.firebase.analytics.FirebaseAnalytics;


public class AnalyticsManager {

    private static AnalyticsManager manager;
    private FirebaseAnalytics firebaseAnalytics;

    private AnalyticsManager() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(Application.getContext());
    }

    public static AnalyticsManager getInstance() {
        if (manager == null) {
            manager = new AnalyticsManager();
        }
        return manager;
    }

    public void sendAnalytics(String actionDetail, String actionName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, (actionDetail));
        bundle.putString(AppConstants.ACTION_TYPE, actionName);
        firebaseAnalytics.logEvent(actionName, bundle);
    }
}
