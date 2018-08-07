package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.AppConstants;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.ArrayList;
import java.util.List;

public class PlayingExercise extends AppCompatActivity {

    public static boolean is_Paused = false;
    public static int pauseTimer = 0;

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
    public float exerciseKcal;
    List<ExerciseDay> exerciseDayList = new ArrayList<>();
    public int currentRound = 0;
    public int currentExercise = 0;
    public int totalRounds = 0;
    public int totaTimeSpend;
    AppDataBase dataBase;
    int totalExercisesPlayed;

    boolean iscomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_exercise);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "exercise_activity_started");

        Intent i = getIntent();
        currentPlan = i.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        currentDay = i.getIntExtra(getApplicationContext().getString(R.string.day_selected), 0);

        downLoaddbData();
    }

    @SuppressLint("StaticFieldLeak")
    private void downLoaddbData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                exerciseDayList = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                if (exerciseDayList.get(0).getExerciseComplete() >= exerciseDayList.get(0).getTotalExercise()) {
                    exerciseDayList.get(0).setRoundCompleted(0);

                    for (ExerciseDay day : exerciseDayList) {
                        if (day.isStatus())
                            day.setStatus(false);

                        totaTimeSpend += day.getReps();
                    }

                    totaTimeSpend *= exerciseDayList.get(0).getRounds();
                    exerciseDayList.get(0).setExerciseComplete(0);
                    exerciseDayList.get(0).setRoundCompleted(0);
                    InsetData insetData = new InsetData();
                    insetData.execute();
                }

                totalExercisePerRound = exerciseDayList.size();

                totalRounds = exerciseDayList.get(0).getRounds();
                totalExercises = exerciseDayList.get(0).getTotalExercise();

                int roundsCleared = exerciseDayList.get(0).getRoundCompleted();
                totalExercisesPlayed = exerciseDayList.get(0).getRoundCompleted();
                int cE = 0;
                for (ExerciseDay day : exerciseDayList) {
                    if (day.isStatus())
                        cE++;
                }
                currentRound = roundsCleared;

                currentExercise = cE;
                int time = exerciseDayList.get(currentExercise).getReps();
                restTime = exerciseDayList.get(currentExercise).getDelay();
                LogHelper.logD("1994:", "rest : " + restTime);
                currentReps = time;
                currentReps *= 1000;
                if (currentExercise < exerciseDayList.size() - 1) {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDayList.get(currentExercise + 1).getDayId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDayList.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDayList.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDayList.get(0).getDayId()).getName();
                }

                int exerciseId = exerciseDayList.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();
                exerciseKcal = dataBase.exerciseDao().findById(exerciseId).getCalories();
                iscomplete = exerciseDayList.get(0).getExerciseComplete() == 5;

                LogHelper.logD("1994:Current round", "" + currentRound);
                LogHelper.logD("1994:Currnet day", "" + currentDay);
                LogHelper.logD("1994:Current Exercise", "" + currentExercise);
                LogHelper.logD("1994:Total Round", "" + exerciseDayList.get(0).getRounds());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isCancelled())
                    return;

                if (exerciseDayList.get(0).getExerciseComplete() >= exerciseDayList.get(0).getTotalExercise())
                    selectFragment(AppConstants.COMPLETE);
                else
                    StartPlayingFragment();

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

    private void selectFragment(int type) {
        boolean isAdd = true;
        Fragment fragment;
        switch (type) {
            case AppConstants.SKIP:
                fragment = new SkipFragment();
                break;
            case AppConstants.EXERCISE:
                fragment = new ExerciseFragment();
                isAdd = false;
                break;
            case AppConstants.PAUSE:
                fragment = new PauseFragment();
                break;
            case AppConstants.NEXT:
                fragment = new NextFragment();
                break;
            case AppConstants.HELP:
                fragment = new HelpFragment();
                break;
            case AppConstants.COMPLETE:
                fragment = new CompleteFragment();
                break;
            default:
                return;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (isAdd) {
            fragmentTransaction.add(R.id.main_screen, fragment);
            fragmentTransaction.addToBackStack(null);
        } else fragmentTransaction.replace(R.id.main_screen, fragment);

        fragmentTransaction.commit();
    }

    public static void resetStaticPauseValues() {
        pauseTimer = 0;
        is_Paused = false;
    }

    public void helpFragmentFun(int remaingTimer) {
        is_Paused = true;
        pauseTimer = remaingTimer;
        selectFragment(AppConstants.HELP);
    }

    public void StartPlayingFragment() {
        if (!iscomplete)
            selectFragment(AppConstants.EXERCISE);
        else
            selectFragment(AppConstants.COMPLETE);
    }

    public void PauseFragment(int remaingTime) {
        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        is_Paused = true;
        pauseTimer = remaingTime;

        selectFragment(AppConstants.PAUSE);
    }

    public void NextFragment() {
        restTime = exerciseDayList.get(currentExercise).getDelay();
        onCompleteCheckingNext();
        selectFragment(AppConstants.NEXT);
    }

    public void onResumeFragment() {
        selectFragment(AppConstants.EXERCISE);
    }

    @SuppressLint("StaticFieldLeak")
    private void onCompleteCheckingNext() {
        exerciseDayList.get(currentExercise).setStatus(true);
        totalExercises = exerciseDayList.size();
        if (currentExercise < totalExercises - 1) {
            Log.i("1994:Current exercise", "current exercise less than total" + currentExercise + "Tot" + totalExercises);
            currentExercise++;
            TTSManager.getInstance(getApplication()).play("Take a Rest for " + restTime + "seconds" + "The Next Exercise is " + nextExerciseName);
        } else {
            if (currentRound < exerciseDayList.get(0).getRounds()) {
                currentRound++;

                AnalyticsManager.getInstance().sendAnalytics("round_complete", "plan " + currentPlan + "day " + currentDay);

                TTSManager.getInstance(getApplication()).play(" This is end of Round " + currentRound + " Take a rest for" + restTime + "seconds ");//. You have" + (exerciseDayList.get(0).getRounds() - currentRound ) +"round remaining"  +"The Next Exercise is "+nextExerciseName);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                TTSManager.getInstance(getApplication()).play("You have" + (exerciseDayList.get(0).getRounds() - currentRound) + "round remaining" + "The Next Exercise is " + nextExerciseName);
                            }
                        }.start();
                    }
                }.execute();

                Log.i("1994:Current exercise", "current rounds less than total");

                for (ExerciseDay exerciseDay : exerciseDayList)
                    exerciseDay.setStatus(false);

                currentExercise = 0;
            }

        }

        if ((currentRound < exerciseDayList.get(0).getRounds())) {

            Log.i("1994:Current round", "Day not updated");

            exerciseDayList.get(0).setExerciseComplete(exerciseDayList.get(0).getExerciseComplete() + 1);

            exerciseDayList.get(0).setRoundCompleted(currentRound);
        } else {
            Log.i("1994:currentDay", "Day Upgraded");

            LogHelper.logD("1994:Current Round", "" + currentRound + "Get Rounds" + (exerciseDayList.get(0).getRounds() - 1));

            exerciseDayList.get(0).setExerciseComplete(exerciseDayList.get(0).getTotalExercise());
            exerciseDayList.get(0).setRoundCompleted(currentRound);
            selectFragment(AppConstants.COMPLETE);
            AnalyticsManager.getInstance().sendAnalytics("complete_all_exercises", "plan " + currentPlan + "day " + currentDay);
            TTSManager.getInstance(getApplication()).play(" Well Done. This is end of day " + (currentDay + 1) + "of your training");
            iscomplete = true;
            InsetData insetData = new InsetData();
            insetData.execute();
            return;

        }

        new InsetData().execute();
        //Your Workout Today will consist of [5] different exercises completed in [3] rounds. You will do each exercise in short intense intervals followed by a rest of few seconds. You will get a rest of [60] seconds at the end of each round.
        LogHelper.logD("1994:Current round", "" + currentRound);
        LogHelper.logD("1994:Currnet day", "" + currentDay);
        LogHelper.logD("1994:Current Exercise", "" + currentExercise);

    }

    @SuppressLint("StaticFieldLeak")
    private void dbLoadData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                exerciseDayList = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);


                //  currentExercise = AppStateManager.currentExercise;
                //currentRound = AppStateManager.roundCleared;

                totalRounds = exerciseDayList.get(0).getRounds();
                totalExercises = exerciseDayList.get(0).getTotalExercise();

                totalExercisePerRound = exerciseDayList.size();

                int roundsCleared = exerciseDayList.get(0).getRoundCompleted();
                totalExercisesPlayed = exerciseDayList.get(0).getRoundCompleted();
                int cE = 0;


                for (ExerciseDay day : exerciseDayList) {
                    if (day.isStatus())
                        cE++;
                }


                currentRound = roundsCleared;
                currentExercise = cE;


                int exerciseId = exerciseDayList.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();
                int time = exerciseDayList.get(currentExercise).getReps();
                //  nextRest = exerciseDayList.get(currentExercise).getDelay();

                LogHelper.logD("1994:", "rest : " + restTime);


                currentReps = time;

                currentReps *= 1000;

                if (currentExercise < exerciseDayList.size() - 1) {

                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDayList.get(currentExercise + 1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDayList.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDayList.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDayList.get(0).getId()).getName();
                }

                LogHelper.logD("1994:Current round", "" + currentRound);
                LogHelper.logD("1994:Currnet day", "" + currentDay);
                LogHelper.logD("1994:Current Exercise", "" + currentExercise);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                selectFragment(AppConstants.SKIP);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }


        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class InsetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dataBase.exerciseDayDao().insertAll(exerciseDayList);
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
    protected void onDestroy() {
        super.onDestroy();

        AnalyticsManager.getInstance().sendAnalytics("Exercise Screen End", "Plan " + currentPlan + " Day " + currentDay + " Total Exercises " + totalExercises + " Total Exercises Done " + totalExercisesPlayed);
        resetStaticPauseValues();
    }

}
