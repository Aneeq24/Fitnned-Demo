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
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DailyExerciseAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
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

    int plan;
    int planDay;
    int completeRounds = 0;
    int completeExercise = 0;
    int totalTimeSpend = 0;
    float kCal = 0;
    Context context;
    DailyExerciseAdapter mAdapter;
    List<Exercise> mEXList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_scroll)
    RecyclerView rvDayExercise;
    @BindView(R.id.tv_exercise)
    TextView tvExercise;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_kcal)
    TextView tvKcal;
    @BindView(R.id.tv_Title)
    TextView tvTitle;
    @BindView(R.id.img_title)
    ImageView imgTitle;

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
        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        planDay = intent.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);
        if (plan == 0)
            tvTitle.setText("Exercise");
        else {
            String[] dayTTS = context.getResources().getStringArray(R.array.days_tts);
            TTSManager.getInstance(getApplication()).play(dayTTS[planDay-1]);
            tvTitle.setText("DAY " + (planDay));
            switch (planDay) {
                case 1:
                case 5:
                case 9:
                case 13:
                case 17:
                case 21:
                case 25:
                case 29:
                    imgTitle.setImageResource(R.drawable.lower_abs);
                    break;
                case 2:
                case 6:
                case 10:
                case 14:
                case 18:
                case 22:
                case 26:
                case 30:
                    imgTitle.setImageResource(R.drawable.obliques);
                    break;
                case 3:
                case 7:
                case 11:
                case 15:
                case 19:
                case 23:
                case 28:
                    imgTitle.setImageResource(R.drawable.upper_abs);
                    break;
            }
        }

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        AdsManager.getInstance().showFacebookInterstitial(getString(R.string.FB_Int_Exercise_List), true);

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_list_activity");

        rvDayExercise.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DailyExerciseAdapter(this);
        rvDayExercise.setNestedScrollingEnabled(false);
        rvDayExercise.setAdapter(mAdapter);
        mAdapter.setDayPlan(planDay, plan);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new getExerciseTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected final Void doInBackground(Void... params) {

            List<ExerciseDay> mList = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(plan, planDay);
            if (mList.size() > 0) {
                completeExercise = mList.get(0).getExerciseComplete();
                completeRounds = mList.get(0).getRoundCompleted();
                for (ExerciseDay day : mList) {
                    if (day.isStatus())
                        day.setStatus(false);
                    totalTimeSpend = totalTimeSpend + day.getReps();
                    Exercise exercise = AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId());
                    if (exercise != null) {
                        mEXList.add(new Exercise(day.getReps(), day.getDelay(), exercise.getName(), exercise.getDisplay()));
                        kCal = kCal + exercise.getCalories();
                    }
                }
                totalTimeSpend = totalTimeSpend * mList.get(0).getRounds();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int totalExercisePerRound = mEXList.size();
            tvExercise.setText(String.valueOf(totalExercisePerRound) + " Exercise");
            int minutes = (totalTimeSpend % 3600) / 60;
            tvTime.setText(String.valueOf(minutes) + " Min");
            tvKcal.setText(String.valueOf((int) kCal) + " Kcal");
            mAdapter.setList(mEXList);
            mAdapter.setData(completeRounds, completeExercise);
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        if (completeRounds > 0 || completeExercise > 0)
            Utils.setCheckBox(context, planDay, plan);
        else Utils.setScreen(context, planDay, plan);
    }
}
