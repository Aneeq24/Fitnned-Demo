package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;

public class Application extends android.app.Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private String TAG = Application.class.getSimpleName();

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        AdsManager.getInstance();
        MobileAds.initialize(context, context.getString(R.string.app_id));

        if (BuildConfig.DEBUG) {
            AnalyticsManager.getInstance().sendAnalytics(TAG, "application_opened");
            AdSettings.addTestDevice("0e82415f-e1df-4d8c-96a6-25247706d495");
            AdsManager.getInstance();
        }
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
}
