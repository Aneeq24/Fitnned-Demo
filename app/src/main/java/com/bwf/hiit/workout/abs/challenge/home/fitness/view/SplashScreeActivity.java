package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Day;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DayProgressModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Detail;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Plan;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.PlanDays;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.JsonUtils;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SplashScreeActivity extends AppCompatActivity {

    private final String TAG = SplashScreeActivity.class.getSimpleName();
    boolean backPresed;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scree);
        context = this;
        AnalyticsManager.getInstance().sendAnalytics("splash_screen_started", "activity_started");
        Stetho.initializeWithDefaults(this);

        new AppDbCheckingTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class AppDbCheckingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPrefHelper.writeInteger(context, "sound", 0);

            Gson gson = new Gson();
            AppDataBase appDataBase = AppDataBase.getInstance();

            User user = new User();
            user.setId(1);
            if (!SharedPrefHelper.readBoolean(context, getString(R.string.is_first_run)))
                appDataBase.userdao().insertAll(user);

            if (appDataBase != null) {
                // insert plans
                if (appDataBase.planDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "plans.json");
                        List<Plan> plans = gson.fromJson(json, new TypeToken<List<Plan>>() {
                        }.getType());
                        if (plans != null && plans.size() > 0)
                            appDataBase.planDao().insertAll(plans);
                    } catch (Exception e) {
                        Log.e(TAG, "plans: " + e.getLocalizedMessage());
                    }
                }

                //insert Day and progress

                if (appDataBase.dayProgressDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "daysprogress.json");
                        List<DayProgressModel> progressModels = gson.fromJson(json, new TypeToken<List<DayProgressModel>>() {
                        }.getType());
                        if (progressModels != null && progressModels.size() > 0)
                            appDataBase.dayProgressDao().insertAll(progressModels);
                    } catch (Exception e) {
                        Log.e(TAG, "dayprogress: " + e.getLocalizedMessage());
                    }
                }

                // insert exercises
                if (appDataBase.exerciseDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "exercises.json");
                        List<Exercise> exercises = gson.fromJson(json, new TypeToken<List<Exercise>>() {
                        }.getType());
                        if (exercises != null && exercises.size() > 0)
                            appDataBase.exerciseDao().insertAll(exercises);
                    } catch (Exception e) {
                        Log.e(TAG, "exercises: " + e.getLocalizedMessage());
                    }
                }

                if (appDataBase.detailDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(context, "details.json");
                        List<Detail> details = gson.fromJson(json, new TypeToken<List<Detail>>() {
                        }.getType());
                        if (details != null && details.size() > 0)
                            appDataBase.detailDao().insertAll(details);
                    } catch (Exception e) {
                        Log.e(TAG, "detail: " + e.getLocalizedMessage());
                    }
                }

                // insert exercises days

                if (appDataBase.exerciseDayDao().getCount() == 0) {
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
                if (backPresed)
                    return;
                if (SharedPrefHelper.readBoolean(context, getString(R.string.is_first_run))) {
                    setDefaultPreferences();
                } else {
                    SharedPrefHelper.writeBoolean(context, getString(R.string.is_first_run), true);
                    startActivity(new Intent(context, SelectGender.class));
                    finish();
                }


            }, 1000);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void setDefaultPreferences() {
        SharedPrefHelper.writeInteger(context, getString(R.string.hour), 15);
        SharedPrefHelper.writeInteger(context, getString(R.string.minute), 5);
        SharedPrefHelper.writeInteger(context, getString(R.string.language), 0);
        SharedPrefHelper.writeInteger(context, "kcal", 0);
        SharedPrefHelper.writeInteger(context, "bmi", 0);
        SharedPrefHelper.writeBoolean(context, "rate", false);
        SharedPrefHelper.writeBoolean(context, "reminder", false);
        SharedPrefHelper.writeBoolean(context, getString(R.string.alarm), true);
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPresed = true;
    }

}
