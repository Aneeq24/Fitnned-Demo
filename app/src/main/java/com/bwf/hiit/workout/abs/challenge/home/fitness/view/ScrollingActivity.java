package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayRecycleAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class ScrollingActivity extends AppCompatActivity {

    int val = 0;
    int plan = 0;
    boolean paused;
    DataModelWorkout dataModelsWorkout;
    DayRecycleAdapter dayRecycleAdapter;
    CircleProgressBar circleProgressBarLeft;
    CircleProgressBar circleProgressBarCompleted;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();

        AdView adVie = findViewById(R.id.banner_day);
        AdView adView = findViewById(R.id.baner_Admob);

        AdsManager.getInstance().showBanner(adVie);
        adVie.setAlpha(0);
        AdsManager.getInstance().showBanner(adView);

        circleProgressBarLeft = findViewById(R.id.line_progress_left);
        circleProgressBarCompleted = findViewById(R.id.line_progress_finished);
        circleProgressBarLeft.setProgressFormatter((progress, max) -> progress + "");

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "day_selection_activity");

        if (getIntent() != null && intent.hasExtra(getString(R.string.plan)))
            plan = getIntent().getIntExtra(getString(R.string.plan), 0);

//        toolbar.setTitle(getPlanName());
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dataModelsWorkout = new DataModelWorkout();
        populateData();

        TTSManager.getInstance(getApplication()).play("You have selected plan " + getPlanName() + "  Mode of 30 Day Ab Challenge");

    }

    @SuppressLint("StaticFieldLeak")
    private void populateData() {
        dataModelsWorkout.curretPlan = plan;
        switch (plan - 1) {
            case 0:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);  //new String[]
                for (int i = 0; i < 30; i++)
                    dataModelsWorkout.progress.add(i, (float) 0);

                break;
            case 1:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);//new String[]
                for (int i = 0; i < 30; i++)
                    dataModelsWorkout.progress.add(i, (float) 0);

                break;
            case 2:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
                for (int i = 0; i < 30; i++)
                    dataModelsWorkout.progress.add(i, (float) 0);
                break;
            default:
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                AppDataBase dataBase = AppDataBase.getInstance();

                for (int i = 0; i < 30; i++) {
                    int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getExerciseComplete();
                    int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getTotalExercise();


                    float v = (float) totalComplete / (float) totalExercises;

                    LogHelper.logD("1994:", "" + v);
                    if (v >= 1) {
                        val++;
                        LogHelper.logD("1994:", "" + val);
                    }

                }

                int dayLeft = 30 - val;
                return String.valueOf(dayLeft);
            }

            @Override
            protected void onPostExecute(String dayLeft) {
                super.onPostExecute(dayLeft);
                if (isCancelled()) {
                    return;
                }
                initView();
                circleProgressBarLeft.setMax(30);
                circleProgressBarLeft.setProgress(Integer.parseInt(dayLeft));
                LogHelper.logD("1993", "Day left" + (dayLeft));
                circleProgressBarCompleted.setMax(30);
                circleProgressBarCompleted.setProgress(val);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

        }.execute();
    }

    private void initView() {
        RecyclerView rvDayTasks = findViewById(R.id.dayTaskRecycleid);
        rvDayTasks.setNestedScrollingEnabled(false);
        rvDayTasks.setLayoutManager(new LinearLayoutManager(this));
        dayRecycleAdapter = new DayRecycleAdapter(dataModelsWorkout);
        rvDayTasks.setAdapter(dayRecycleAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            val = 0;
            populateData();
            dayRecycleAdapter.resetAdapter(dataModelsWorkout);
        }
    }

    private String getPlanName() {
        int i = plan - 1;
        switch (i) {
            case 0:
                return "Beginner";
            case 1:
                return "Intermediate";
            case 2:
                return "Advanced";
            default:
                return "";
        }
    }

    private void resetData() {
        dataModelsWorkout.dayName = null;
        dataModelsWorkout.iconForExcersice = null;
        dataModelsWorkout.progress = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetData();
    }
}
