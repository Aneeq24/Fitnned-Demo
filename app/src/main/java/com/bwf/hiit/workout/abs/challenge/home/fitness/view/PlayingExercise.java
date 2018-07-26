package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.ExerciseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.HelpFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.NextFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.PauseFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.SkipFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.ArrayList;
import java.util.List;

public class PlayingExercise extends AppCompatActivity {

    public static  FragmentManager fragmentManager;

   // public  static  PlayingExercise instance;

    public  static boolean is_Paused = false;
    public  static  int pauseTimer = 0;
    PauseFragment pauseFragment = new PauseFragment();
    ExerciseFragment exerciseFragment = new ExerciseFragment();
    SkipFragment skipFragment = new SkipFragment();
    NextFragment nextFragment = new NextFragment();
    HelpFragment helpFragment = new HelpFragment();

    FragmentTransaction fragmentTransaction;

    public  int currentReps;
    public  int totalExercises;

    public  int currentPlan = 0;
    public  int currentDay = 0;

    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_exercise);

//        if (instance == null)
//            instance = this;

        DataModelWorkout dataModelWorkout = new DataModelWorkout();
        fragmentManager = getSupportFragmentManager();

        AnalyticsManager.getInstance().sendAnalytics("Activity Started", "Playing Exercise Activity");

        //TODO
        AdsManager.getInstance().showFacebookInterstitialAd();

        Intent i = getIntent();
        currentPlan = i.getIntExtra(getApplicationContext().getString(R.string.plan),0);
        currentDay = i.getIntExtra(getApplicationContext().getString(R.string.day_selected),0);


        downLoaddbData();

        sharedPreferences = this.getSharedPreferences(String.valueOf(getApplicationContext()), Context.MODE_PRIVATE);

    }

    List<ExerciseDay> exerciseDays = new ArrayList<ExerciseDay>();

    int currentRound = 0;
    int currentExercise = 0;
    AppDataBase dataBase;
    int totalExercisesPlayed;


    @SuppressLint("StaticFieldLeak")
    void  downLoaddbData()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
               dataBase = AppDataBase.getInstance();
               exerciseDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);


              //  currentExercise = AppStateManager.currentExercise;

                //currentRound = AppStateManager.roundCleared;

//                int totalRounds = exerciseDays.get(0).getRounds();
//                int totalExercises =  exerciseDays.get(0).getTotalExercise();

                int roundsCleared = exerciseDays.get(0).getRoundCompleted();
                totalExercisesPlayed =exerciseDays.get(0).getRoundCompleted();
                int cE = 0;


                for (ExerciseDay day: exerciseDays)
                {
                    if (day.isStatus())
                        cE++;
                }

                currentRound = roundsCleared;
                currentExercise = cE;

                int time = exerciseDays.get(currentExercise).getReps();


                currentReps = time;

                currentReps*=1000;


                return null;
            }

            @Override
            protected void onPostExecute (Void aVoid){
                super.onPostExecute(aVoid);
                StartSkipFragment();
//                initExerciseList();
            }

            @Override
            protected void onProgressUpdate (Void...values){
                super.onProgressUpdate(values);
            }


        }.execute();

    }


    public  int  getCurrentReps()
    {
        return  currentReps;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetStaticPauseValues();
    }

    public static void  resetStaticPauseValues() {
        pauseTimer = 0;
        is_Paused = false;
    }

    public  void  StartSkipFragment() {

        fragmentTransaction  = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container , skipFragment ,  null);
        fragmentTransaction.commit();

    }

    public  void  helpFragmentFun(int remaingTimer)
    {
        is_Paused = true;
        pauseTimer = remaingTimer;
        fragmentManager.beginTransaction().replace(R.id.fragment_container ,helpFragment,null).commit();
    }

    public void StartPlayingFragment()
    {
        fragmentManager.beginTransaction().replace(R.id.fragment_container ,exerciseFragment,null).commit();
    }

    public  void  PauseFragment(int remaingTime)
    {
        is_Paused = true;
        pauseTimer = remaingTime;
        fragmentManager.beginTransaction().replace(R.id.fragment_container ,pauseFragment,null).commit();
    }

    public  void  NextFragment()
    {
        onCompleteCheckingNext();
        fragmentManager.beginTransaction().replace(R.id.fragment_container , nextFragment,null).commit();

    }

    public  void  onResumeFragment()
    {
        fragmentManager.beginTransaction().replace(R.id.fragment_container , exerciseFragment,null).commit();
    }

    void  onCompleteCheckingNext()
    {


        exerciseDays.get(currentExercise).setStatus(true);



        totalExercises = exerciseDays.size();



        if (currentExercise<totalExercises-1)
        {
            Log.i("1994:Current exercise" , "current exercise less than total" + currentExercise + "Tot" + totalExercises);
            currentExercise++;
        }
        else
        {
            if ( currentRound < exerciseDays.get(0).getRounds()-1)
            {
                currentRound++;
                Log.i("1994:Current exercise" , "current rounds less than total");

                for (ExerciseDay exerciseDay: exerciseDays)
                {
                    exerciseDay.setStatus(false);
                }

                currentExercise = 0;
            }


        }

        if (currentRound < exerciseDays.get(0).getRounds()-1)
        {
            Log.i("1994:currentDay" , "Day not updated");
            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getExerciseComplete() + 1);

            exerciseDays.get(0).setRoundCompleted(currentRound);
        }
        else
        {
            currentDay++;
            Log.i("1994:currentDay" , "Day Upgraded");
            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getTotalExercise());
        }


        InsetData insetData = new InsetData();

        insetData.execute();



      //  AppStateManager.currentExercise  = currentExercise;



    }

    public  void  skipRestNextButtonClicked()
    {

    }


    public  class  InsetData extends AsyncTask<Void , Void ,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            dataBase.exerciseDayDao().insertAll(exerciseDays);





            return null;
        }
    }




}
