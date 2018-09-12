package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;

public class Application extends android.app.Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("32044b73-433f-4c52-80d0-041e8800e364");
        }
        context = getApplicationContext();
        MobileAds.initialize(context, context.getString(R.string.app_id));
        AdsManager.getInstance();
        //hello
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
