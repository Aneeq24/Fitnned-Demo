package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.ExerciseDayViewModel;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyExerciseInfo extends AppCompatActivity {

    Context context;
    DailyExerciseAdapter mAdapter;
    List<Exercise> mEXList;
    int plan;
    int planday;
    int totalRounds;
    int completeRounds;
    int completeExercise;
    float kcal = 0;
    List<ExerciseDay> mList;
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

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_list_activity");

        ExerciseDayViewModel mExerciseDayViewModel = ViewModelProviders.of(this).get(ExerciseDayViewModel.class);

        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        planday = intent.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);

        rvDayExercise.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DailyExerciseAdapter(this);
        rvDayExercise.setAdapter(mAdapter);
        mAdapter.setDayPlan(planday, plan);

        mExerciseDayViewModel.getExerciseDays(plan, planday).observe(this, exerciseDayList -> {
            if (exerciseDayList != null) {
//                if (exerciseDays.get(0).getExerciseComplete() >= exerciseDays.get(0).getTotalExercise()) {
//                    exerciseDays.get(0).setRoundCompleted(0);
//
//                    for (ExerciseDay day : exerciseDays) {
//                        if (day.isStatus())
//                            day.setStatus(false);
//
//                    }
//                    exerciseDays.get(0).setExerciseComplete(0);
//                    exerciseDays.get(0).setRoundCompleted(0);
//                }
                mList = exerciseDayList;
                int totaTimeSpend = 0;
                for (ExerciseDay day : exerciseDayList) {
                    if (day.isStatus())
                        day.setStatus(false);
                    totaTimeSpend = totaTimeSpend + day.getReps() + day.getDelay();
                }
                totaTimeSpend = totaTimeSpend * exerciseDayList.get(0).getRounds();
                totalRounds = exerciseDayList.get(0).getRounds();
                int totalExercisePerRound = exerciseDayList.size();
//                int roundsCleared = exerciseDayList.get(0).getRoundCompleted();
//                int cE = 0;
//                for (ExerciseDay day : exerciseDayList) {
//                    if (day.isStatus())
//                        cE++;
//                }
                completeExercise = exerciseDayList.get(0).getExerciseComplete();
                completeRounds = exerciseDayList.get(0).getRoundCompleted();
                tvRound.setText(String.valueOf(totalRounds) + "x");
                tvExercise.setText(String.valueOf(totalExercisePerRound));
                int minutes = (totaTimeSpend % 3600) / 60;
                tvTime.setText(String.valueOf(minutes));
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new getExerciseTask(mList).execute();
            }
        }, 1000);

    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {
        private List<ExerciseDay> mList;

        getExerciseTask(List<ExerciseDay> mList) {
            this.mList = mList;
        }

        @Override
        protected final Void doInBackground(Void... params) {
            for (ExerciseDay day : mList) {
                Exercise exercise = AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId());
                if (exercise != null) {
                    mEXList.add(new Exercise(day.getReps(), day.getDelay(), exercise.getName(), exercise.getDisplay_name()));
                    kcal = kcal + exercise.getCalories();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvKcal.setText(String.valueOf((int) kcal * totalRounds));
            mAdapter.setList(mEXList);
            mAdapter.setData(completeRounds,completeExercise);
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        if (completeRounds > 0 || completeExercise > 0)
            Utils.setCheckBox(context, planday, plan);
        else Utils.setScreen(context, planday, plan);
    }
}
