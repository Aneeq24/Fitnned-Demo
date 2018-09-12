package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.CompleteFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.ExerciseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.HelpFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.NextFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.PauseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.RestFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.SkipFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.inapp.MyBilling;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayingExercise extends AppCompatActivity {

    @BindView(R.id.progressBar)
    public SegmentedProgressBar progressBar;
    private String[] title = {"BEGINNER", "INTERMEDIATE", "ADVANCED"};

    public MyBilling mBilling;
    FragmentManager fragmentManager;
    AppDataBase dataBase;
    PauseFragment pauseFragment = new PauseFragment();
    ExerciseFragment exerciseFragment = new ExerciseFragment();
    SkipFragment skipFragment = new SkipFragment();
    NextFragment nextFragment = new NextFragment();
    HelpFragment helpFragment = new HelpFragment();
    CompleteFragment completeFragment = new CompleteFragment();
    RestFragment restFragment = new RestFragment();

    public int restTime;
    public int currentReps;
    public int currentDay = 0;
    public static int pauseTimer = 0;
    public int totalExercisePerRound = 0;
    public int currentPlan = 0;
    public int totalExercises = 0;
    public String displayName;
    public String exerciseName;
    public String nextExerciseName;
    public String nextExerciseImage;
    public List<ExerciseDay> mListExDays;
    public int currentEx = 0;
    public int timer = 0;
    public int currentRound = 0;
    public int totalTimeSpend = 0;
    public float totalKcal = 0;
    public boolean isComplete;
    public static boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_exercise);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        fragmentManager = getSupportFragmentManager();
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_activity_started");

        Intent i = getIntent();
        currentPlan = i.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        currentDay = i.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);

        mBilling = new MyBilling(this);
        mBilling.onCreate();
        mListExDays = new ArrayList<>();
        dataBase = AppDataBase.getInstance();

        getData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                mListExDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                if (mListExDays.size() > 0) {

                    for (ExerciseDay day : mListExDays) {
                        totalTimeSpend = totalTimeSpend + day.getReps();
                        totalKcal = totalKcal + AppDataBase.getInstance().exerciseDao().findByIdbg(day.getId()).getCalories();
                        if (day.isStatus()) {
                            currentEx++;
                        }
                    }
                    if (currentEx == mListExDays.size())
                        currentEx--;
                    totalExercisePerRound = mListExDays.get(0).getTotalExercise();
                    totalExercises = mListExDays.get(0).getTotalExercise();
                    currentReps = mListExDays.get(currentEx).getReps();
                    restTime = mListExDays.get(currentEx).getDelay();

                    currentReps *= 1000;
                    if (currentEx < mListExDays.size() - 1) {
                        nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentEx + 1).getId()).getDisplay_name();
                        nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentEx + 1).getId()).getName();
                    } else {
                        nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getDisplay_name();
                        nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getName();
                    }

                    int exerciseId = mListExDays.get(currentEx).getId();
                    exerciseName = dataBase.exerciseDao().findByIdbg(exerciseId).getName();
                    displayName = dataBase.exerciseDao().findByIdbg(exerciseId).getDisplay_name();

                    timer = totalTimeSpend;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isCancelled())
                    return;
                if (mListExDays.size() > 0) {
                    if (mListExDays.get(0).getExerciseComplete() >= mListExDays.get(0).getTotalExercise()) {
                        progressBar.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("repeat", true);
                        completeFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().add(R.id.fragment_container, completeFragment, null).commitAllowingStateLoss();
                        isComplete = true;
                    } else {
                        progressBar.setSegmentCount(totalExercisePerRound);
                        if (currentEx > 0) {
                            for (int i = 0; i < currentEx; i++)
                                progressBar.incrementCompletedSegments();
                        }
                        fragmentManager.beginTransaction().add(R.id.fragment_container, skipFragment, null).commitAllowingStateLoss();
                        TTSManager.getInstance(getApplication()).play("This is start of today workout The exercise is  " + displayName);
                    }
                } else {
                    isComplete = true;
                    progressBar.setVisibility(View.GONE);
                    fragmentManager.beginTransaction().add(R.id.fragment_container, restFragment, null).commitAllowingStateLoss();
                    TTSManager.getInstance(getApplication()).play(getString(R.string.txt_rest));
                }
            }
        }.execute();
    }

    public int getCurrentReps() {
        return currentReps;
    }

    public void helpFragmentFun(int remaingTimer) {
        isPaused = true;
        pauseTimer = remaingTimer;
        fragmentManager.beginTransaction().replace(R.id.fragment_container, helpFragment, null).commitAllowingStateLoss();
    }

    public void StartPlayingFragment() {
        if (!isComplete)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, exerciseFragment, null).commitAllowingStateLoss();
        else {
            AnalyticsManager.getInstance().sendAnalytics("exercise_complete", "plan_" + title[currentPlan - 1] + "day_" + currentDay + "completed");
            progressBar.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean("repeat", false);
            completeFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, completeFragment, null).commitAllowingStateLoss();
        }
    }

    public void PauseFragment(int remaingTime) {
        AdsManager.getInstance().showFacebookInterstitialAd();
        isPaused = true;
        pauseTimer = remaingTime;
        fragmentManager.beginTransaction().replace(R.id.fragment_container, pauseFragment, null).commitAllowingStateLoss();
    }

    public void NextFragment(boolean isNext, int time) {
        timer = time;
        restTime = mListExDays.get(currentEx).getDelay();
        onCompleteCheckingNext(isNext);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, nextFragment, null).commitAllowingStateLoss();
    }

    public void onResumeFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, exerciseFragment, null).commit();
    }

    public void onCompleteCheckingNext(boolean isNext) {
        AnalyticsManager.getInstance().sendAnalytics("exercise_complete", "plan_" + title[currentPlan - 1] + "day_" + currentDay + "exercise_" + (currentEx + 1));
        mListExDays.get(currentEx).setStatus(true);
        totalExercises = mListExDays.size();

        if (!isNext) {
            if (currentEx > 0) {
                Log.i("1994:Current exercise", "current exercise greater than zera" + currentEx + "Total" + totalExercises);
                currentEx--;
            } else
                Toast.makeText(this, "No more previous Exercise", Toast.LENGTH_SHORT).show();
        } else if (currentEx < totalExercises - 1) {
            Log.i("1994:Current exercise", "current exercise less than total" + currentEx + "Total" + totalExercises);
            currentEx++;
            if (!mListExDays.get(currentEx).isStatus())
                progressBar.incrementCompletedSegments();
        } else if (currentRound < mListExDays.get(0).getRounds()) {
            currentRound++;
        }

        if ((currentRound < mListExDays.get(0).getRounds())) {
            mListExDays.get(0).setExerciseComplete(mListExDays.get(0).getExerciseComplete() + 1);
            mListExDays.get(0).setRoundCompleted(currentRound);
        } else {
            AnalyticsManager.getInstance().sendAnalytics("plan " + currentPlan + "days " + currentDay, "complete_all_exercises");
            mListExDays.get(0).setExerciseComplete(mListExDays.get(0).getTotalExercise());
            mListExDays.get(0).setRoundCompleted(currentRound);
            this.isComplete = true;
        }

        new updateData().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (skipFragment != null && skipFragment.isVisible()) {
            if (!skipFragment.pause)
                skipFragment.pauseOrRenume();
        } else if (exerciseFragment != null && exerciseFragment.isVisible()) {
            exerciseFragment.pause();
        } else if (nextFragment != null && nextFragment.isVisible()) {
            if (!nextFragment.pause)
                nextFragment.pauseOrRenume();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isComplete) {
            AnalyticsManager.getInstance().sendAnalytics("playing_exercise", "close at" + "plan " + currentPlan + "days " + currentDay);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.app_name));
            alertDialogBuilder
                    .setMessage("Do you want to end workout ?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialog, id) -> {
                        dialog.cancel();
                        finish();
                    }).setNegativeButton("NO", (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListExDays.size() > 0) {
            AnalyticsManager.getInstance().sendAnalytics("Exercise_Screen_End", "plan_" + title[currentPlan - 1] + "day_" +
                    currentDay + "exercises_" + mListExDays.get(0).getExerciseComplete());
        } else {
            AnalyticsManager.getInstance().sendAnalytics("Exercise_Screen_End", "plan_" + title[currentPlan - 1] + "day_" +
                    currentDay + "rest_time");
        }
        resetStaticPauseValues();
    }

    public void resetStaticPauseValues() {
        pauseTimer = 0;
        isPaused = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBilling.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("StaticFieldLeak")
    public class updateData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            dataBase.exerciseDayDao().insertAll(mListExDays);

            currentRound = mListExDays.get(0).getRoundCompleted();

            int exerciseId = mListExDays.get(currentEx).getId();
            exerciseName = dataBase.exerciseDao().findByIdbg(exerciseId).getName();
            displayName = dataBase.exerciseDao().findByIdbg(exerciseId).getDisplay_name();
            currentReps = mListExDays.get(currentEx).getReps();
            currentReps *= 1000;

            if (currentEx < mListExDays.size() - 1) {
                nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentEx + 1).getId()).getDisplay_name();
                nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentEx + 1).getId()).getName();
            } else {
                nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getDisplay_name();
                nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getName();
            }
            return null;
        }
    }
}
