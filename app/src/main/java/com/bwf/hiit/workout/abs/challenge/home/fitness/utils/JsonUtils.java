package com.bwf.hiit.workout.abs.challenge.home.fitness.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;


public class JsonUtils {

    private final static String TAG = JsonUtils.class.getSimpleName();

    public static String readJsonFromAssets(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "readJsonFromAssets: " + ex.getLocalizedMessage());
            return null;
        }
    }
}
