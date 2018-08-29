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
        MobileAds.initialize(context, context.getString(R.string.app_id));

        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("EE78FA08C9A4B2CD1C07DE12DB2BFD16");
            AdsManager.getInstance();
        } else
            AdsManager.getInstance();
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
