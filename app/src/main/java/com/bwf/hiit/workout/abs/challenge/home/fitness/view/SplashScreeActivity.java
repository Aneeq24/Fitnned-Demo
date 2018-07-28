package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AppPrefManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Day;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DayProgressModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Detail;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Plan;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.PlanDays;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.JsonUtils;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SplashScreeActivity extends AppCompatActivity {

    private final String TAG = SplashScreeActivity.class.getSimpleName();

    boolean backPresed;

    class AppDbCheckingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Gson gson = new Gson();
            AppDataBase appDataBase = AppDataBase.getInstance();
            if (appDataBase != null) {
                // insert plans
                if (appDataBase.planDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(getApplicationContext(), "plans.json");
                        List<Plan> plans = gson.fromJson(json, new TypeToken<List<Plan>>() {
                        }.getType());
                        if (plans != null && plans.size() > 0) {
                            appDataBase.planDao().insertAll(plans);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "plans: " + e.getLocalizedMessage());
                    }
                }

                //insert Day and progress

                if(appDataBase.dayProgressDao().getCount() == 0)
                {
                    try {
                        String json = JsonUtils.readJsonFromAssets(getApplicationContext(), "daysprogress.json");
                        List<DayProgressModel> progressModels = gson.fromJson(json, new TypeToken<List<DayProgressModel>>() {
                        }.getType());
                        if (progressModels != null && progressModels.size() > 0) {
                            appDataBase.dayProgressDao().insertAll(progressModels);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "dayprogress: " + e.getLocalizedMessage());
                    }
                }

                // insert exercises
                if (appDataBase.exerciseDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(getApplicationContext(), "exercises.json");
                        List<Exercise> exercises = gson.fromJson(json, new TypeToken<List<Exercise>>() {
                        }.getType());
                        if (exercises != null && exercises.size() > 0) {
                            appDataBase.exerciseDao().insertAll(exercises);

                        }
                    } catch (Exception e) {
                        Log.e(TAG, "exercises: " + e.getLocalizedMessage());
                    }
                }

                if (appDataBase.detailDao().getCount() == 0)
                {
                    try {
                        String json = JsonUtils.readJsonFromAssets(getApplicationContext(), "details.json");
                        List<Detail> details = gson.fromJson(json, new TypeToken<List<Detail>>() {
                        }.getType());
                        if (details != null && details.size() > 0) {
                            appDataBase.detailDao().insertAll(details);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "detail: " + e.getLocalizedMessage());
                    }
                }

                // insert exercises days

                if (appDataBase.exerciseDayDao().getCount() == 0) {
                    try {
                        String json = JsonUtils.readJsonFromAssets(getApplicationContext(), "days.json");
                        List<PlanDays> planDays = gson.fromJson(json, new TypeToken<List<PlanDays>>() {
                        }.getType());
                        if (planDays != null && planDays.size() > 0) {
                            for (PlanDays days : planDays) {
                                for (Day day : days.getDays()) {


                                    for (ExerciseDay exerciseDay : day.getExercisesOfDay()) {
                                        exerciseDay.setPlanId(days.getPlanId());
                                        exerciseDay.setDayId(day.getDayId());
                                        exerciseDay.setTotalExercise(day.getExercisesOfDay().size() * exerciseDay.getRounds());
                                    }
                                    appDataBase.exerciseDayDao().insertAll(day.getExercisesOfDay());

                                }
                            }
//                            appDataBase.exerciseDayDao().insertAll(planDays);
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

            //TODO

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (backPresed)
                        return;

                    Intent newActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(newActivity);
                    finish();
                }
            }, 2000);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    class  InsertPrefrences extends  AsyncTask<Void,Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            AppDataBase dataBase = AppDataBase.getInstance();

            if (dataBase != null)
            {

                for(int i = 0; i<dataBase.planDao().getCount() ; i++)
                {
                    for (int j = 0 ; j <dataBase.exerciseDayDao().getCount() ; j++)
                    {
                        for (int k = 0; k<dataBase.exerciseDao().getCount(); k ++)
                        {

                        }
                    }
                }

            }
            return null;
        }
    }

    private int splashTimeOut = 15000;   //Time in mili seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scree);

        AppPrefManager.getInstance().initPref(getApplicationContext());

        Stetho.initializeWithDefaults(this);

        AnalyticsManager.getInstance().sendAnalytics("Activity Started", "Splash Activity");

        AppDbCheckingTask appDbCheckingTask = new AppDbCheckingTask();
        appDbCheckingTask.execute();

//         new Handler().postDelayed(new Runnable()
//         {
//             @Override
//             public void run()
//             {
//                 Intent newActivity = new Intent(getApplicationContext() , MainActivity.class);
//                 startActivity(newActivity);
//                 finish();
//             }
//           },splashTimeOut
//
//
//        );


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        backPresed = true;
    }

    AppDataBase dataBase;

    void testingDb() {

        int val = dataBase.planDao().getCount();
        int dayCount = dataBase.planDao().getCount();
        int exercise = dataBase.exerciseDao().getCount();


        if (val == 0)
        {

        }
        if (dayCount == 0)
        {

        }

        if (exercise == 0)
        {

        }

    }


}
