package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.ArrayList;
import java.util.List;

public class PlayingExercise extends AppCompatActivity {

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
    public List<ExerciseDay> exerciseDays = new ArrayList<>();
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

        downLoaddbData();
    }

    @SuppressLint("StaticFieldLeak")
    private void downLoaddbData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                exerciseDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                if (exerciseDays.get(0).getExerciseComplete() >= exerciseDays.get(0).getTotalExercise()) {
                    exerciseDays.get(0).setRoundCompleted(0);

                    for (ExerciseDay day : exerciseDays) {
                        if (day.isStatus())
                            day.setStatus(false);
                        totaTimeSpend = totaTimeSpend + day.getReps();
                    }

                    totaTimeSpend = totaTimeSpend * exerciseDays.get(0).getRounds();

//                    exerciseDays.get(0).setExerciseComplete(0);
//                    exerciseDays.get(0).setRoundCompleted(0);
                    new InsetData().execute();
                }

                totalExercisePerRound = exerciseDays.size();

                totalRounds = exerciseDays.get(0).getRounds();
                totalExercises = exerciseDays.get(0).getTotalExercise();

                int roundsCleared = exerciseDays.get(0).getRoundCompleted();
                totalExercisesPlayed = exerciseDays.get(0).getExerciseComplete();
                int cE = 0;
                for (ExerciseDay day : exerciseDays) {
                    if (day.isStatus())
                        cE++;
                }
                currentRound = roundsCleared;

                currentExercise = cE;
                int time = exerciseDays.get(currentExercise).getReps();
                restTime = exerciseDays.get(currentExercise).getDelay();

                LogHelper.logD("1994:", "rest : " + restTime);
                currentReps = time;
                currentReps *= 1000;
                if (currentExercise < exerciseDays.size() - 1) {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise + 1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getName();
                }

                int exerciseId = exerciseDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();
                exerciseKcal = (int) dataBase.exerciseDao().findById(exerciseId).getCalories();
                LogHelper.logD("1994:Current round", "" + currentRound);
                LogHelper.logD("1994:Currnet day", "" + currentDay);
                LogHelper.logD("1994:Current Exercise", "" + currentExercise);
                LogHelper.logD("1994:Total Round", "" + exerciseDays.get(0).getRounds());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isCancelled())
                    return;

                if (exerciseDays.get(0).getExerciseComplete() >= exerciseDays.get(0).getTotalExercise()) {
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

        //TODO Analytics
        AnalyticsManager.getInstance().sendAnalytics("Exercise Screen End", "Plan " + currentPlan + " Day " + currentDay + " Total Exercises " + totalExercises + " Total Exercises Done " + totalExercisesPlayed);

        resetStaticPauseValues();

        if (AdsManager.getInstance().isFacebookInterstitalLoaded()) {
            AdsManager.getInstance().showFacebookInterstitialAd();
        } else {
            AdsManager.getInstance().showInterstitialAd();
        }
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
        restTime = exerciseDays.get(currentExercise).getDelay();
        onCompleteCheckingNext();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, nextFragment, null).commit();
    }

    public void onResumeFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, exerciseFragment, null).commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void onCompleteCheckingNext() {
        exerciseDays.get(currentExercise).setStatus(true);
        totalExercises = exerciseDays.size();
        if (currentExercise < totalExercises - 1) {
            Log.i("1994:Current exercise", "current exercise less than total" + currentExercise + "Tot" + totalExercises);
            currentExercise++;
        } else {
            if (currentRound < exerciseDays.get(0).getRounds()) {

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (nextFragment != null && nextFragment.isVisible() && !nextFragment.pause) {
                            TTSManager.getInstance(getApplication()).play("This is end of Round " + currentRound +
                                    "You have" + (exerciseDays.get(0).getRounds() - currentRound) + "round remaining" + "The Next Exercise is " + nextExerciseName);
                            AnalyticsManager.getInstance().sendAnalytics("round_complete" + currentRound, "plan " + currentPlan + "day " + currentDay);
                            currentExercise = 0;
                        }
                    }
                }.start();

                for (ExerciseDay exerciseDay : exerciseDays) {
                    exerciseDay.setStatus(false);
                }

                currentRound++;
                Log.i("1994:Current exercise", "current rounds less than total");
            }

        }

        if ((currentRound < exerciseDays.get(0).getRounds())) {

            Log.i("1994:Current round", "Day not updated");

            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getExerciseComplete() + 1);
            exerciseDays.get(0).setRoundCompleted(currentRound);
        } else {
            Log.i("1994:currentDay", "Day Upgraded");

            LogHelper.logD("1994:Current Round", "" + currentRound + "Get Rounds" + (exerciseDays.get(0).getRounds() - 1));

            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getTotalExercise());
            exerciseDays.get(0).setRoundCompleted(currentRound);
            fragmentTransaction.add(R.id.fragment_container, completeFragment, null);

            //TODO Analytics
            AnalyticsManager.getInstance().sendAnalytics("complete_all_exercises", "plan " + currentPlan + "day " + currentDay);

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
                exerciseDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                totalRounds = exerciseDays.get(0).getRounds();
                totalExercises = exerciseDays.get(0).getTotalExercise();

                totalExercisePerRound = exerciseDays.size();

                int roundsCleared = exerciseDays.get(0).getRoundCompleted();
                totalExercisesPlayed = exerciseDays.get(0).getExerciseComplete();
                int cE = 0;

                for (ExerciseDay day : exerciseDays) {
                    if (day.isStatus())
                        cE++;
                }

                currentRound = roundsCleared;
                currentExercise = cE;

                int exerciseId = exerciseDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();
                int time = exerciseDays.get(currentExercise).getReps();

                LogHelper.logD("1994:", "rest : " + restTime);

                currentReps = time;
                currentReps *= 1000;

                if (currentExercise < exerciseDays.size() - 1) {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise + 1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise + 1).getId()).getName();
                } else {
                    nextExerciseName = dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getName();
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

            dataBase.exerciseDayDao().insertAll(exerciseDays);

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
        AnalyticsManager.getInstance().sendAnalytics("playing_exercise", "close at" +  "plan " + currentPlan + "day " + currentDay);
        super.onBackPressed();
    }
}
