package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class SharedPrefHelper {

    public static final String BREATH_REMINDER = "reminder_breath";
    public static final String BREATH_REMINDER_TIME = "reminder_breath_time";

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

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static long readLong(Context context, String key) {
        return getPreferences(context).getLong(key, 0);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value);
    }

    public static long getBreathReminderTime(Context context) {
        return readLong(context, BREATH_REMINDER_TIME);
    }

    public static void setBreathReminderTime(Context context, long value) {
        writeLong(context, BREATH_REMINDER_TIME, value);
    }

    public static int getBreathReminder(Context context) {
        return readInteger(context, BREATH_REMINDER, -1);
    }

    public static void setBreathReminder(Context context, int value) {
        writeInteger(context, BREATH_REMINDER, value);
    }
}
