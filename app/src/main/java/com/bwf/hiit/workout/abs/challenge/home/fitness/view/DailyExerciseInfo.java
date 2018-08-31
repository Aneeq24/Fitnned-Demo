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
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DailyExerciseAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyExerciseInfo extends AppCompatActivity {

    Context context;
    DailyExerciseAdapter mAdapter;
    List<Exercise> mEXList;
    int plan;
    int planday;
    int totalRounds = 0;
    int completeRounds = 0;
    int completeExercise = 0;
    int totaTimeSpend = 0;
    float kcal = 0;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_dailyExercise)
    RecyclerView rvDayExercise;
    @BindView(R.id.tv_round)
    TextView tvRound;
    @BindView(R.id.tv_exercise)
    TextView tvExercise;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_kcal)
    TextView tvKcal;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_exercise_info);
        ButterKnife.bind(this);

        context = this;
        mEXList = new ArrayList<>();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        AdsManager.getInstance().showFacebookInterstitialAd();
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_list_activity");


        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        planday = intent.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);

        rvDayExercise.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DailyExerciseAdapter(this);
        rvDayExercise.setAdapter(mAdapter);
        mAdapter.setDayPlan(planday, plan);

        new getExerciseTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected final Void doInBackground(Void... params) {

            List<ExerciseDay> mList = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(plan, planday);
            completeExercise = mList.get(0).getExerciseComplete();
            completeRounds = mList.get(0).getRoundCompleted();
            for (ExerciseDay day : mList) {
                if (day.isStatus())
                    day.setStatus(false);
                totaTimeSpend = totaTimeSpend + day.getReps() + day.getDelay();
                Exercise exercise = AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId());
                if (exercise != null) {
                    mEXList.add(new Exercise(day.getReps(), day.getDelay(), exercise.getName(), exercise.getDisplay_name()));
                    kcal = kcal + exercise.getCalories();
                }
            }

            totaTimeSpend = totaTimeSpend * mList.get(0).getRounds();
            totalRounds = mList.get(0).getRounds();

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int totalExercisePerRound = mEXList.size();
            tvRound.setText(String.valueOf(totalRounds) + "x");
            tvExercise.setText(String.valueOf(totalExercisePerRound));
            int minutes = (totaTimeSpend % 3600) / 60;
            tvTime.setText(String.valueOf(minutes));
            tvKcal.setText(String.valueOf((int) kcal * totalRounds));
            mAdapter.setList(mEXList);
            mAdapter.setData(completeRounds, completeExercise);
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        if (completeRounds > 0 || completeExercise > 0)
            Utils.setCheckBox(context, planday, plan);
        else Utils.setScreen(context, planday, plan);
    }
}
