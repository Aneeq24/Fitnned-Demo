package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    boolean isStart = true;
    boolean isMove = true;
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
    @BindView(R.id.startButton)
    Button startButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ly_download)
    LinearLayout lyDownload;
    AdView adView;

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
        if (plan == 0) {
            String[] dayTTS = context.getResources().getStringArray(R.array.exercise_list);
            switch (planDay - 1) {
                case 0:
                    tvTitle.setText(dayTTS[0]);
                    imgTitle.setImageResource(R.drawable.workout_screen_pre_workout_warm_up);
                    break;
                case 1:
                    tvTitle.setText(dayTTS[1]);
                    imgTitle.setImageResource(R.drawable.workout_screen_post_workout_cool_down);
                    break;
                case 2:
                    tvTitle.setText(dayTTS[2]);
                    imgTitle.setImageResource(R.drawable.workout_screen_5_min_plank_challenge_image);
                    break;
                case 3:
                    tvTitle.setText(dayTTS[3]);
                    imgTitle.setImageResource(R.drawable.workout_screen_two_minute_abs_image);
                    break;
            }
        } else {
            String[] dayTTS = context.getResources().getStringArray(R.array.days_tts);
            TTSManager.getInstance(getApplication()).play(dayTTS[planDay - 1]);
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

        adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        AdsManager.getInstance().showInterstitialAd(getString(R.string.AM_Int_Main_Menu));

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_list_activity");

        rvDayExercise.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new DailyExerciseAdapter(this);
        rvDayExercise.setNestedScrollingEnabled(false);
        rvDayExercise.setAdapter(mAdapter);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new getExerciseTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class getExerciseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected final Void doInBackground(Void... params) {

            if (planDay > 1 && plan != 0) {
                List<ExerciseDay> mPrev = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(plan, planDay - 1);
                if (mPrev.size() > 0) {
                    if (mPrev.get(0).getExerciseComplete() != mPrev.get(0).getTotalExercise()) {
                        isStart = false;
                    }
                } else {
                    mPrev = AppDataBase.getInstance().exerciseDayDao().getExerciseDays(plan, planDay - 2);
                    if (mPrev.get(0).getExerciseComplete() != mPrev.get(0).getTotalExercise()) {
                        isStart = false;
                    }
                }

            }
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
                        exercise.setUnit(mList.get(0).getReps());
                        mEXList.add(exercise);
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
            if (plan != 0) {
                if (!isStart) {
                    startButton.setBackgroundResource(R.drawable.ic_gray_round_bar);
                    startButton.setText("Workout Locked");
                }
            }
        }
    }

    @OnClick(R.id.startButton)
    public void onViewClicked() {
        for (Exercise exercise : mEXList) {
            if (!exercise.isOnline()) {
                isMove = false;
            }
        }
        if (isStart) {
            if (isMove) {
                setActivity();
            } else if (Utils.isNetworkAvailable(context)) {
                lyDownload.setVisibility(View.VISIBLE);
                adView.setVisibility(View.GONE);
                new getTask().execute();
                for (int i = 0; i < 10000; i++)
                    progressBar.setProgress(i);
            } else
                Utils.showConnectionDialog(context);
        } else {
            if (plan != 0)
                Utils.showPreDoneDialog(context);
        }
    }

    private void setActivity() {
        if (completeRounds > 0 || completeExercise > 0)
            Utils.setCheckBox(context, planDay, plan);
        else Utils.setScreen(context, planDay, plan);
    }

    @SuppressLint("StaticFieldLeak")
    private class getTask extends AsyncTask<Void, Void, Void> {

        @SuppressLint("CheckResult")
        @Override
        protected final Void doInBackground(Void... params) {
            for (Exercise exercise : mEXList) {
                if (!exercise.isOnline()) {
                    Glide.with(context).load(exercise.getUrl()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            exercise.setOnline(false);
                            AppDataBase.getInstance().exerciseDao().update(exercise);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            exercise.setOnline(true);
                            AppDataBase.getInstance().exerciseDao().update(exercise);
                            return false;
                        }
                    });
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Utils.setScreen(context, planDay, plan);
            lyDownload.setVisibility(View.GONE);
            adView.setVisibility(View.VISIBLE);
        }
    }
}
