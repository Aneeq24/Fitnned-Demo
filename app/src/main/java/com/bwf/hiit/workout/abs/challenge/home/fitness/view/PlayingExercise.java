package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.CompleteFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.ExerciseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.HelpFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.NextFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.PauseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.SkipFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.inapp.MyBilling;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.ArrayList;
import java.util.List;

public class PlayingExercise extends AppCompatActivity {

    public MyBilling mBilling;

    public static FragmentManager fragmentManager;

    public static boolean is_Paused = false;
    public static int pauseTimer = 0;
    PauseFragment pauseFragment = new PauseFragment();
    ExerciseFragment exerciseFragment = new ExerciseFragment();
    SkipFragment skipFragment = new SkipFragment();
    NextFragment nextFragment = new NextFragment();
    HelpFragment helpFragment = new HelpFragment();
    CompleteFragment completeFragment = new CompleteFragment();

    FragmentTransaction fragmentTransaction;

    public int currentReps;
    public int totalExercises = 0;
    public int totalExercisePerRound = 0;
    public int currentPlan = 0;
    public int currentDay = 0;
    public int restTime;
    public String exerciseName;
    public String displayName;
    public String nextExerciseName;
    public String nextExerciseImage;
    public List<ExerciseDay> mListExDays ;
    public int currentRound = 0;
    public int currentExercise = 0;
    public int totalRounds = 0;
    public int totaTimeSpend = 0;
    AppDataBase dataBase;
    public int totalExercisesPlayed;
    public int exerciseKcal;
    boolean iscomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_exercise);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        fragmentManager = getSupportFragmentManager();
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_activity_started");

        Intent i = getIntent();
        currentPlan = i.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        currentDay = i.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);

        mBilling = new MyBilling(this);
        mBilling.onCreate();
        mListExDays = new ArrayList<>();

        downLoaddbData();
    }

    @SuppressLint("StaticFieldLeak")
    private void downLoaddbData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                mListExDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                for (ExerciseDay day : mListExDays) {
//                    if (day.isStatus())
//                        day.setStatus(false);
                    totaTimeSpend = totaTimeSpend + day.getReps() + day.getDelay();
                }
                totaTimeSpend = totaTimeSpend * mListExDays.get(0).getRounds();
                mListExDays.get(0).setExerciseComplete(mListExDays.get(0).getExerciseComplete());
                mListExDays.get(0).setRoundCompleted(mListExDays.get(0).getRoundCompleted());
//                mListExDays.get(0).setExerciseComplete(0);
//                mListExDays.get(0).setRoundCompleted(0);

                new InsetData().execute();

                totalExercisePerRound = mListExDays.size();
                totalRounds = mListExDays.get(0).getRounds();
                totalExercises = mListExDays.get(0).getTotalExercise();
                int roundsCleared = mListExDays.get(0).getRoundCompleted();
                totalExercisesPlayed = mListExDays.get(0).getExerciseComplete();
                int cE = 0;
                for (ExerciseDay day : mListExDays) {
                    if (day.isStatus())
                        cE++;
                }
                currentRound = roundsCleared;
                currentExercise = cE;
                int time = mListExDays.get(currentExercise).getReps();
                restTime = mListExDays.get(currentExercise).getDelay();

                LogHelper.logD("1994:", "rest : " + restTime);
                currentReps = time;
                currentReps *= 1000;
                if (currentExercise < mListExDays.size() - 1) {
                    nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentExercise + 1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getName();
                }

                int exerciseId = mListExDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findByIdbg(exerciseId).getName();
                displayName = dataBase.exerciseDao().findByIdbg(exerciseId).getDisplay_name();
                exerciseKcal = (int) dataBase.exerciseDao().findByIdbg(exerciseId).getCalories();
                LogHelper.logD("1994:Current round", "" + currentRound);
                LogHelper.logD("1994:Currnet day", "" + currentDay);
                LogHelper.logD("1994:Current Exercise", "" + currentExercise);
                LogHelper.logD("1994:Total Round", "" + mListExDays.get(0).getRounds());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isCancelled())
                    return;

                if (mListExDays.get(0).getExerciseComplete() >= mListExDays.get(0).getTotalExercise()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, completeFragment, null);
                    fragmentTransaction.commit();
                } else
                    StartSkipFragment();

                int i = currentRound + 1;

                TTSManager.getInstance(getApplication()).play("This is start of round" + i + "  next exercise is  " + displayName);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

        }.execute();

    }

    public int getCurrentReps() {
        return currentReps;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AnalyticsManager.getInstance().sendAnalytics("Exercise Screen End", "Plan " + currentPlan + " Day " + currentDay + " Total Exercises " + totalExercises + " Total Exercises Done " + totalExercisesPlayed);
        resetStaticPauseValues();
    }

    public static void resetStaticPauseValues() {
        pauseTimer = 0;
        is_Paused = false;
    }

    public void StartSkipFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, skipFragment, null);
        fragmentTransaction.commit();

    }

    public void helpFragmentFun(int remaingTimer) {
        is_Paused = true;
        pauseTimer = remaingTimer;
        fragmentManager.beginTransaction().replace(R.id.fragment_container, helpFragment, null).commit();
    }

    public void StartPlayingFragment() {
        if (!iscomplete)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, exerciseFragment, null).commit();
        else
            fragmentManager.beginTransaction().replace(R.id.fragment_container, completeFragment, null).commit();
    }

    public void PauseFragment(int remaingTime) {

        if (AdsManager.getInstance().isFacebookInterstitalLoaded()) {
            AdsManager.getInstance().showFacebookInterstitialAd();
        } else {
            AdsManager.getInstance().showInterstitialAd();
        }

        is_Paused = true;
        pauseTimer = remaingTime;

        fragmentManager.beginTransaction().replace(R.id.fragment_container, pauseFragment, null).commit();
    }

    public void NextFragment() {
        restTime = mListExDays.get(currentExercise).getDelay();
        onCompleteCheckingNext();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, nextFragment, null).commit();
    }

    public void onResumeFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, exerciseFragment, null).commit();
    }

    @SuppressLint("StaticFieldLeak")
    public void onCompleteCheckingNext() {
        mListExDays.get(currentExercise).setStatus(true);
        totalExercises = mListExDays.size();
        if (currentExercise < totalExercises - 1) {
            Log.i("1994:Current exercise", "current exercise less than total" + currentExercise + "Tot" + totalExercises);
            currentExercise++;
        } else {
            if (currentRound < mListExDays.get(0).getRounds()) {
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (currentRound < totalRounds)
                            TTSManager.getInstance(getApplication()).play("This is end of Round " + currentRound +
                                    "You have" + (mListExDays.get(0).getRounds() - currentRound) + "round remaining");
                        AnalyticsManager.getInstance().sendAnalytics("plan " + currentPlan + "day " + currentDay, "round_complete" + currentRound);
                    }
                }.start();

                for (ExerciseDay exerciseDay : mListExDays) {
                    exerciseDay.setStatus(false);
                }
                currentExercise = 0;
                currentRound++;

                Log.i("1994:Current exercise", "current rounds less than total");
            }

        }

        if ((currentRound < mListExDays.get(0).getRounds())) {
            Log.i("1994:Current round", "Day not updated");
            mListExDays.get(0).setExerciseComplete(mListExDays.get(0).getExerciseComplete() + 1);
            mListExDays.get(0).setRoundCompleted(currentRound);
            AnalyticsManager.getInstance().sendAnalytics("plan " + currentPlan + "day " + currentDay, "round_complete" + currentRound);
        } else {
            Log.i("1994:currentDay", "Day Upgraded");
            LogHelper.logD("1994:Current Round", "" + currentRound + "Get Rounds" + (mListExDays.get(0).getRounds() - 1));
            AnalyticsManager.getInstance().sendAnalytics("plan " + currentPlan + "day " + currentDay, "complete_all_exercises");
            mListExDays.get(0).setExerciseComplete(mListExDays.get(0).getTotalExercise());
            mListExDays.get(0).setRoundCompleted(currentRound);
            fragmentTransaction.add(R.id.fragment_container, completeFragment, null);
            iscomplete = true;
            new InsetData().execute();
            return;
        }
        new InsetData().execute();
        //Your Workout Today will consist of [5] different exercises completed in [3] rounds. You will do each exercise in short intense intervals followed by a rest of few seconds. You will get a rest of [60] seconds at the end of each round.
        LogHelper.logD("1994:Current round", "" + currentRound);
        LogHelper.logD("1994:Currnet day", "" + currentDay);
        LogHelper.logD("1994:Current Exercise", "" + currentExercise);
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

    @SuppressLint("StaticFieldLeak")
    private void dbLoadData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                mListExDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                totalRounds = mListExDays.get(0).getRounds();
                totalExercises = mListExDays.get(0).getTotalExercise();

                totalExercisePerRound = mListExDays.size();

                int roundsCleared = mListExDays.get(0).getRoundCompleted();
                totalExercisesPlayed = mListExDays.get(0).getExerciseComplete();
                int cE = 0;

                for (ExerciseDay day : mListExDays) {
                    if (day.isStatus())
                        cE++;
                }

                currentRound = roundsCleared;
                currentExercise = cE;

                int exerciseId = mListExDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findByIdbg(exerciseId).getName();
                displayName = dataBase.exerciseDao().findByIdbg(exerciseId).getDisplay_name();
                int time = mListExDays.get(currentExercise).getReps();

                LogHelper.logD("1994:", "rest : " + restTime);

                currentReps = time;
                currentReps *= 1000;

                if (currentExercise < mListExDays.size() - 1) {
                    nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentExercise + 1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findByIdbg(mListExDays.get(0).getId()).getName();
                }

                LogHelper.logD("1994:Current round", "" + currentRound);
                LogHelper.logD("1994:Currnet day", "" + currentDay);
                LogHelper.logD("1994:Current Exercise", "" + currentExercise);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    public class InsetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dataBase.exerciseDayDao().insertAll(mListExDays);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            if (!iscomplete)
                dbLoadData();
        }
    }

    @Override
    public void onBackPressed() {
        AnalyticsManager.getInstance().sendAnalytics("playing_exercise", "close at" + "plan " + currentPlan + "day " + currentDay);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder
                .setMessage("Do you want to exit workout ?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, id) -> {
                    dialog.cancel();
                    finish();
                }).setNegativeButton("NO", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBilling.onActivityResult(requestCode, resultCode, data);
    }

}
