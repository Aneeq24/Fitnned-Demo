package com.bwf.hiit.workout.abs.challenge.home.fitness;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;

public class Application extends android.app.Application {

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
   /*      MobileAds.initialize(context,context.getString(R.string.app_id));
        if (BuildConfig.DEBUG) {
              AdSettings.addTestDevice("728E481201E977FE91F3F915B469D33D");}
            AdsManager.getInstance();

            AnalyticsManager.getInstance().sendAnalytics(TAG, "application_opened");
        }*/
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
