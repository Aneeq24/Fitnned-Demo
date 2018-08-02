package com.bwf.hiit.workout.abs.challenge.home.fitness.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AppPrefManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {

    private final static String TAG = Utils.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static String formatNumber(float number) {
        DecimalFormat format = new DecimalFormat(".##");
        return format.format(number);
    }

    public static String formatName(String name) {
        if (TextUtils.isEmpty(name))
            return "";
        else {
            StringBuilder stringBuilder = new StringBuilder();
            String[] parts;
            if (name.contains("_")) {
                parts = name.split("_");
                for (String part : parts) {
                    stringBuilder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1, part.length())).append(" ");
                }
            } else {
                stringBuilder.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1, name.length()));
            }
            return stringBuilder.toString();
        }
    }

    public static void playAudio(Context context, int resourceId) {


        try {

            int i = AppPrefManager.getInstance().getValue("sound",0);
            if (i>0)
                return;

            MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
            mediaPlayer.setOnCompletionListener(MediaPlayer -> mediaPlayer.release());
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "playAudio: " + e.getLocalizedMessage());
        }
    }

    public static long getCurrentDateInMillis() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        long currentDateInMillis = -1;
        try {
            currentDateInMillis = dateFormat.parse(dateFormat.format(new Date())).getTime();
        } catch (ParseException e) {
            Log.e(TAG, "exception in getting current date in millis: " + e.getLocalizedMessage());
        }
        return currentDateInMillis;
    }

    public static String convertLongDateToString(long date) {
        return new SimpleDateFormat("d MMM", Locale.getDefault()).format(new Date(date));
    }

    public static void exportDB(Context context) {
        try {
            File internal = Environment.getDataDirectory();
            File external = Environment.getExternalStorageDirectory();

            if (external.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + context.getString(R.string.app_name);
                String backupDBPath = "/Backup/" + context.getString(R.string.app_name) + ".db";
                File currentDB = new File(internal, currentDBPath);
                File backupDB = new File(external, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.d(TAG, "DB exported successfully");
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to export DB");
        }
    }
}
