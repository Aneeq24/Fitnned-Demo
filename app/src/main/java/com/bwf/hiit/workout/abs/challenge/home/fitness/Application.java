package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;

public class Application extends android.app.Application {

    private static Context context;
    private  String TAG =Application.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        if (BuildConfig.DEBUG)
            AdSettings.addTestDevice("3dbbcd0d-15e0-4081-85ec-07b314e3fd00");

        MobileAds.initialize(context,context.getString(R.string.app_id));

        AdsManager.getInstance();
        AnalyticsManager.getInstance().sendAnalytics(TAG, "Application Opened");
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }

    public static Context getContext(){
        return context;
    }
}
