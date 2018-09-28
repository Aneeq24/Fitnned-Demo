package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Day;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.PlanDays;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.JsonUtils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.fabric.sdk.android.Fabric;


public class SplashScreeActivity extends AppCompatActivity {

    private final String TAG = SplashScreeActivity.class.getSimpleName();
    int dbVersion;
    Context context;
    boolean backPressed;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, crashlytics);
        AnalyticsManager.getInstance().sendAnalytics("splash_screen_started", "activity_started");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Abs");
        if (Utils.isNetworkAvailable(Application.getContext())) {
            mDatabase.child("version").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String value = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        dbVersion = Integer.parseInt(value);
                    } catch (NullPointerException e) {
                        dbVersion = 0;  //After this, id is 1
                        e.printStackTrace();
                    }
                    setDatabase();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else
            new AppDbCheckingTask().execute();

        getZipFile();
//      Stetho.initializeWithDefaults(this);
    }

    private void setDatabase() {
        if (dbVersion == 1) {
            mDatabase.child("tables").child("exercises").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<Exercise>> genericTypeIndicator = new GenericTypeIndicator<List<Exercise>>() {
                    };
                    List<Exercise> hashMap = dataSnapshot.getValue(genericTypeIndicator);
                    new updateDatabase(hashMap).execute();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            new AppDbCheckingTask().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AppDbCheckingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Gson gson = new Gson();
            AppDataBase appDataBase = AppDataBase.getInstance();

            if (appDataBase != null) {
                if (appDataBase.userdao().getUser() == 0) {
                    User user = new User();
                    Reminder reminder = new Reminder();
                    reminder.setFriday(true);
                    reminder.setSatday(true);
                    reminder.setSunday(true);
                    reminder.setMonday(true);
                    reminder.setTuesday(true);
                    reminder.setWednesday(true);
                    reminder.setThursday(true);
                    appDataBase.userdao().insertAll(user);
                    appDataBase.reminderDao().insertAll(reminder);
                    // insert exercises
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "exercises.json");
                        List<Exercise> exercises = gson.fromJson(json, new TypeToken<List<Exercise>>() {
                        }.getType());
                        if (exercises != null && exercises.size() > 0) {
                            appDataBase.exerciseDao().insertAll(exercises);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "exercises: " + e.getLocalizedMessage());
                    }
                    // insert exercises days
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "days.json");
                        List<PlanDays> planDays = gson.fromJson(json, new TypeToken<List<PlanDays>>() {
                        }.getType());
                        if (planDays != null && planDays.size() > 0) {
                            for (PlanDays days : planDays) {
                                for (Day day : days.getDays()) {
                                    List<ExerciseDay> mList = new ArrayList<>();
                                    for (ExerciseDay exerciseDay : day.getExercisesOfDay()) {
                                        exerciseDay.setPlanId(days.getPlanId());
                                        exerciseDay.setDayId(day.getDayId());
                                        exerciseDay.setRoundCompleted(day.getRoundCompleted());
                                        exerciseDay.setTotalExercise(day.getTotalExercise());
                                        exerciseDay.setTotalExercise(day.getExercisesOfDay().size() * exerciseDay.getRounds());
                                        exerciseDay.setTotalKcal(0);
                                        mList.add(exerciseDay);
                                    }
                                    appDataBase.exerciseDayDao().insertAll(mList);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "days: " + e.getLocalizedMessage());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            new Handler().postDelayed(() -> {
                if (backPressed)
                    return;
                if (!SharedPrefHelper.readBoolean(context, getString(R.string.is_first_run))) {
                    setDefaultPreferences();
                } else {
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                }
            }, 5000);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class updateDatabase extends AsyncTask<Void, Void, Void> {

        List<Exercise> exercises;

        updateDatabase(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase appDataBase = AppDataBase.getInstance();
            if (appDataBase != null) {
                if (appDataBase.userdao().getUser() == 0) {
                    User user = new User();
                    Reminder reminder = new Reminder();
                    reminder.setFriday(true);
                    reminder.setSatday(true);
                    reminder.setSunday(true);
                    reminder.setMonday(true);
                    reminder.setTuesday(true);
                    reminder.setWednesday(true);
                    reminder.setThursday(true);
                    appDataBase.userdao().insertAll(user);
                    appDataBase.reminderDao().insertAll(reminder);
                    // insert exercises
                    try {
                        if (exercises != null && exercises.size() > 0) {
                            appDataBase.exerciseDao().insertAll(exercises);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "exercises: " + e.getLocalizedMessage());
                    }
                    // insert exercises days
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "days.json");
                        List<PlanDays> planDays = new Gson().fromJson(json, new TypeToken<List<PlanDays>>() {
                        }.getType());
                        if (planDays != null && planDays.size() > 0) {
                            for (PlanDays days : planDays) {
                                for (Day day : days.getDays()) {
                                    List<ExerciseDay> mList = new ArrayList<>();
                                    for (ExerciseDay exerciseDay : day.getExercisesOfDay()) {
                                        exerciseDay.setPlanId(days.getPlanId());
                                        exerciseDay.setDayId(day.getDayId());
                                        exerciseDay.setRoundCompleted(day.getRoundCompleted());
                                        exerciseDay.setTotalExercise(day.getTotalExercise());
                                        exerciseDay.setTotalExercise(day.getExercisesOfDay().size() * exerciseDay.getRounds());
                                        exerciseDay.setTotalKcal(0);
                                        mList.add(exerciseDay);
                                    }
                                    appDataBase.exerciseDayDao().insertAll(mList);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "days: " + e.getLocalizedMessage());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            new Handler().postDelayed(() -> {
                if (backPressed)
                    return;
                if (!SharedPrefHelper.readBoolean(context, getString(R.string.is_first_run))) {
                    setDefaultPreferences();
                } else {
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                }
            }, 5000);
        }
    }

    private void setDefaultPreferences() {
        SharedPrefHelper.writeInteger(context, getString(R.string.hour), 19);
        SharedPrefHelper.writeInteger(context, getString(R.string.minute), 0);
        SharedPrefHelper.writeBoolean(context, "rate", false);
        SharedPrefHelper.writeInteger(context, "sound", 0);
        SharedPrefHelper.writeBoolean(context, "reminder", true);
        SharedPrefHelper.writeBoolean(context, getString(R.string.alarm), true);
        SharedPrefHelper.writeBoolean(context, getString(R.string.is_first_run), true);
        startActivity(new Intent(context, SelectGender.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true;
    }

    private void getZipFile() {
        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("data/data.zip");

        try {
            File localFile = File.createTempFile("images", "zip");
            islandRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Local temp file has been created
                try {
                    unzip(localFile, context.getCacheDir().getAbsolutePath());
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

    public void unzip(File zipFile, String targetDirectory) throws IOException {
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
            SharedPrefHelper.writeBoolean(context, getString(R.string.is_load), true);
        }
    }
}
