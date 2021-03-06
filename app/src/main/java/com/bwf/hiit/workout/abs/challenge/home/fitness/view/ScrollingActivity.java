package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayRecycleAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScrollingActivity extends AppCompatActivity {

    private String[] title = {"Exercise", "BEGINNER", "INTERMEDIATE", "ADVANCED"};
    int val = 0;
    int plan = 0;
    int size = 0;
    boolean paused;
    Context context;
    AdView adView;
    List<Float> mProgress;
    CircleProgressBar progressLeft;
    CircleProgressBar progressCompleted;
    @BindView(R.id.tv_Title)
    TextView tvTitle;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);

        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        adView = findViewById(R.id.baner_Admob);

        progressLeft = findViewById(R.id.line_progress_left);
        progressCompleted = findViewById(R.id.line_progress_finished);
        progressLeft.setProgressFormatter((progress, max) -> progress + "");

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "day_selection_activity");
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.plan)))
            plan = intent.getIntExtra(getString(R.string.plan), 0);

        if (plan == 0)
            size = 4;
        else size = 30;

        tvTitle.setText(title[plan]);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            getData();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        AdsManager.getInstance().showBanner(adView);
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
        mProgress = new ArrayList<>();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    AppDataBase dataBase = AppDataBase.getInstance();
                    for (int i = 0; i < size; i++) {
                        if (dataBase.exerciseDayDao().getExerciseDays(plan, i + 1).size() > 0) {
                            int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan, i + 1).get(0).getExerciseComplete();
                            int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan, i + 1).get(0).getTotalExercise();
                            float v = (float) totalComplete / (float) totalExercises;
                            mProgress.add(v);

                            if (v >= 1) {
                                val++;
                            }
                        } else mProgress.add(200f);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                int dayLeft = size - val;
                return String.valueOf(dayLeft);
            }

            @Override
            protected void onPostExecute(String dayLeft) {
                super.onPostExecute(dayLeft);
                initView();
                progressLeft.setMax(size);
                progressLeft.setProgress(Integer.parseInt(dayLeft));
                progressCompleted.setMax(size);
                progressCompleted.setProgress(val);
            }

        }.execute();
    }

    private void initView() {
        RecyclerView rvDayTasks = findViewById(R.id.rv_scroll);
        rvDayTasks.setNestedScrollingEnabled(false);
        rvDayTasks.setLayoutManager(new LinearLayoutManager(context));
        DayRecycleAdapter dayRecycleAdapter = new DayRecycleAdapter(context, mProgress, plan);
        rvDayTasks.setAdapter(dayRecycleAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            val = 0;
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        AdsManager.getInstance().showInterstitialAd(getString(R.string.AM_Int_Main_Menu));
        super.onBackPressed();
    }

}
