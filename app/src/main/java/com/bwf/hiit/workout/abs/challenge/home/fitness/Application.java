package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;

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

        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        MobileAds.initialize(context, context.getString(R.string.app_id));
        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevice("728E481201E977FE91F3F915B469D33D");
            AdsManager.getInstance();
            AnalyticsManager.getInstance().sendAnalytics(TAG, "application_opened");
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
