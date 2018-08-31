package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class SharedPrefHelper {

    private SharedPrefHelper() {
    }

    @SuppressWarnings("deprecation")
    private static final int MODE = Context.MODE_PRIVATE;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.prefs), MODE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key) {
        return getPreferences(context).getInt(key, 0);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key) {
        return getPreferences(context).getString(key, null);
    }

    public static void clearAll(Context context){
        getEditor(context).clear().commit();
    }

    public static void writeBoolean(Context context,String key, boolean value) {
        getPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static boolean readBoolean(Context context,String key) {
        return getPreferences(context).getBoolean(key, false);
    }

}
