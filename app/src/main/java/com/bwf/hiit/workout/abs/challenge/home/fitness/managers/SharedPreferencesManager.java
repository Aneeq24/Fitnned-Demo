package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;


public class SharedPreferencesManager {

    private SharedPreferences sharedPreferences;
    private static SharedPreferencesManager appPreferences;


    private SharedPreferencesManager() {
    }

    public static SharedPreferencesManager getInstance() {
        if (appPreferences == null) {
            appPreferences = new SharedPreferencesManager();
            if (appPreferences.sharedPreferences == null) {
                Context context = Application.getContext();
                appPreferences.sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
            }
        }
        return appPreferences;
    }

    public void removeKey(String key) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(key).apply();
        }
    }

    public void clear() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }

    public boolean contains(String key) {
        if (sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, Application.getContext().getString(R.string.key_not_found));
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }
}
