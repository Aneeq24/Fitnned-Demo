package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefManager {
    private static AppPrefManager instance;

    boolean isInit = false;

    SharedPreferences sharedPreferences;

    public static AppPrefManager getInstance()
    {
        if (instance == null)
            instance = new AppPrefManager();

        return  instance;
    }

    boolean  checkInint()
    {
        return  isInit;
    }

    public  void  initPref(Context context)
    {
        if (!isInit)
        {
            sharedPreferences  = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            isInit = true;
        }
        else
        {
            return;
        }

    }


    public  void  setValue(String key , int value)
    {
        sharedPreferences.edit().putInt(key,value).apply();
    }

    public  void  setValue(String key , String value)
    {
        sharedPreferences.edit().putString(key,value).apply();

    }

    public  String  getValue(String key)
    {

        String val = sharedPreferences.getString(key , "");
        return val;
    }

    public int getValue(String key , int defaultValue)
    {
        if (!sharedPreferences.contains(key))
            return 0;
        else
        {
            int val =sharedPreferences.getInt(key, 1);
            return  val;
        }
    }


}
