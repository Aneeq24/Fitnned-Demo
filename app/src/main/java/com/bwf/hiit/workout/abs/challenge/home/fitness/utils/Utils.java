package com.bwf.hiit.workout.abs.challenge.home.fitness.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

    private final static String TAG = Utils.class.getSimpleName();
    private static Dialog dialog = null;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static void playAudio(Context context, int resourceId) {

        try {
            int i = SharedPrefHelper.readInteger(context, "sound");
            if (i > 0)
                return;
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
            mediaPlayer.setOnCompletionListener(MediaPlayer -> mediaPlayer.release());
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "playAudio: " + e.getLocalizedMessage());
        }
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

    public static void setCheckBox(Context context, int currentDay, int currentPlan) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.app_name));
        alertDialogBuilder
                .setMessage("You have Play it previously.Do you want to resume?")
                .setCancelable(false)
                .setNegativeButton("Reset", (dialog, id) -> {
                    dialog.cancel();
                    AnalyticsManager.getInstance().sendAnalytics("playing_exercise_reset", "Reset");
                    resetData(context, currentDay, currentPlan);
                }).setPositiveButton("Resume", (dialog, id) -> {
            AnalyticsManager.getInstance().sendAnalytics("playing_exercise_resume", "Resume");
            setScreen(context, currentDay, currentPlan);
            dialog.cancel();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private static void resetData(Context context, int currentDay, int currentPlan) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<ExerciseDay> exerciseDays = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(currentPlan, currentDay);
                for (ExerciseDay day : exerciseDays) {
                    if (day.isStatus())
                        day.setStatus(false);
                }
                exerciseDays.get(0).setExerciseComplete(0);
                exerciseDays.get(0).setRoundCompleted(0);
                AppDataBase.getInstance().exerciseDayDao().insertAll(exerciseDays);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setScreen(context, currentDay, currentPlan);
            }
        }.execute();
    }

    public static void setScreen(Context context, int currentDay, int currentPlan) {
        Intent i = new Intent(context, PlayingExercise.class);
        i.putExtra(context.getString(R.string.day_selected), currentDay);
        i.putExtra(context.getString(R.string.plan), currentPlan);
        context.startActivity(i);
    }

    public static void showConnectionUsDialog(Context context) {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_connection);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        }
        Button btnOk = dialog.findViewById(R.id.btn_rate_us);
        btnOk.setOnClickListener(view1 -> {
            dialog.dismiss();
            dialog = null;
        });
    }

    public static void showRateUsDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rate_us);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
        Button btnSubmit = dialog.findViewById(R.id.btn_rate_us);
        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnSubmit.setOnClickListener(view1 -> onRateUs(context));
        btnClose.setOnClickListener(view1 -> dialog.dismiss());
    }

    public static void onRateUs(Context context) {
        AnalyticsManager.getInstance().sendAnalytics("rate_us_clicked_done", "Rate_us");
        SharedPrefHelper.writeBoolean(context, "rate", true);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        }
    }


    public static void getZipFile() {
        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("data/data.zip");

        try {
            File localFile = File.createTempFile("images", "zip");
            islandRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Local temp file has been created
                try {
                    unzip(localFile, Application.getContext().getCacheDir().getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).addOnFailureListener(exception -> {
                // Handle any errors
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unzip(File zipFile, String targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }
            SharedPrefHelper.writeBoolean(Application.getContext(),
                    Application.getContext().getString(R.string.is_load), true);
        }
    }

}
