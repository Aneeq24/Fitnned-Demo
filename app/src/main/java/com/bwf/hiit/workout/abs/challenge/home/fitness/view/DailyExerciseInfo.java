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
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.ExerciseDayViewModel;
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

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_dailyExercise)
    RecyclerView rvDayExercise;
    @BindView(R.id.tv_round)
    TextView tvRound;
    @BindView(R.id.tv_exercise)
    TextView tvExercise;

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
                int totalRounds = exerciseDayList.get(0).getRounds();
                int totalExercisePerRound = exerciseDayList.size();
//                int roundsCleared = exerciseDayList.get(0).getRoundCompleted();
//                int cE = 0;
//                for (ExerciseDay day : exerciseDayList) {
//                    if (day.isStatus())
//                        cE++;
//                }

                tvRound.setText(String.valueOf(totalRounds));
                tvExercise.setText(String.valueOf(totalExercisePerRound));
                new getExerciseTask(exerciseDayList).execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {
        private List<ExerciseDay> mList;

        getExerciseTask(List<ExerciseDay> mList) {
            this.mList = mList;
        }

        @Override
        protected final Void doInBackground( Void... params) {
            for (ExerciseDay day : mList) {
                Exercise exercise = AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId());
                if (exercise != null) {
                    mEXList.add(new Exercise(day.getReps(),day.getDelay(),exercise.getName(),exercise.getDisplay_name()));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.setList(mEXList);
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        Intent i = new Intent(context, PlayingExercise.class);
        i.putExtra(context.getString(R.string.day_selected), planday);
        i.putExtra(context.getString(R.string.plan), plan);
        startActivity(i);
    }
}
