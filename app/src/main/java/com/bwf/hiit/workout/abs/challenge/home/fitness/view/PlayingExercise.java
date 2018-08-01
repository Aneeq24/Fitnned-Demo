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
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
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
    CompleteFragment completeFragment = new CompleteFragment();

    FragmentTransaction fragmentTransaction;

    public  int currentReps;
    public  int totalExercises = 0;
    public  int totalExercisePerRound = 0;
    public  int currentPlan = 0;
    public  int currentDay = 0;
    public  int restTime;
    public  int nextRest;
    public  String exerciseName;
    public  String displayName;
    public  String nextExerciseName;
    public  String nextExerciseImage;
    SharedPreferences sharedPreferences;
    List<ExerciseDay> exerciseDays = new ArrayList<ExerciseDay>();
    public int currentRound = 0;
    public int currentExercise = 0;
    public int totalRounds = 0;
    public  int totaTimeSpend;
    AppDataBase dataBase;
    int totalExercisesPlayed;

    boolean iscomplete;

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

        AnalyticsManager.getInstance().sendAnalytics("Activity Started", "Starting Exercise Activity");



        Intent i = getIntent();
        currentPlan = i.getIntExtra(getApplicationContext().getString(R.string.plan),0);
        currentDay = i.getIntExtra(getApplicationContext().getString(R.string.day_selected),0);


        downLoaddbData();

        sharedPreferences = this.getSharedPreferences(String.valueOf(getApplicationContext()), Context.MODE_PRIVATE);

    }



    @SuppressLint("StaticFieldLeak")
    void  downLoaddbData()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
               dataBase = AppDataBase.getInstance();
               exerciseDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);

                if (exerciseDays.get(0).getExerciseComplete() >= exerciseDays.get(0).getTotalExercise())
                {
                    exerciseDays.get(0).setRoundCompleted(0);

                    for (ExerciseDay day: exerciseDays)
                    {
                        if (day.isStatus())
                            day.setStatus(false);

                        totaTimeSpend += day.getReps();

                    }

                    totaTimeSpend *= exerciseDays.get(0).getRounds();

                    exerciseDays.get(0).setExerciseComplete(0);
                    exerciseDays.get(0).setRoundCompleted(0);
                    InsetData insetData = new InsetData();
                    insetData.execute();
                }

                totalExercisePerRound = exerciseDays.size();

                totalRounds = exerciseDays.get(0).getRounds();
                totalExercises =  exerciseDays.get(0).getTotalExercise();

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
                restTime = exerciseDays.get(currentExercise).getDelay();
                LogHelper.logD("1994:" , "rest : " + restTime);
                currentReps = time;
                currentReps*=1000;
                if (currentExercise<exerciseDays.size()-1)
                {

                    nextExerciseName =  dataBase.exerciseDao().findById(exerciseDays.get(currentExercise+1).getId()).getDisplay_name();

                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise+1).getId()).getName();
                }
                else
                {
                    nextExerciseName =  dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage =  dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getName();
                }

                int exerciseId = exerciseDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();

                LogHelper.logD("1994:Current round" , "" +currentRound);
                LogHelper.logD("1994:Currnet day" , ""  + currentDay);
                LogHelper.logD("1994:Current Exercise" ,"" + currentExercise);
                LogHelper.logD("1994:Total Round" ,"" + exerciseDays.get(0).getRounds());

                return null;
            }

            @Override
            protected void onPostExecute (Void aVoid){
                super.onPostExecute(aVoid);
                if(isCancelled())
                    return;

                if (exerciseDays.get(0).getExerciseComplete() >= exerciseDays.get(0).getTotalExercise())
                {
                    fragmentTransaction  = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container , completeFragment ,  null);
                    fragmentTransaction.commit();


                }
                else {
                    StartSkipFragment();
                }


                int i = currentRound +1;

                TTSManager.getInstance(getApplication()).play("This is start of round" + i);
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

        //TODO Analytics
        AnalyticsManager.getInstance().sendAnalytics("Exercise Screen End", "Plan " + currentPlan + " Day " + currentDay + " Total Exercises " + totalExercises + " Total Exercises Done " + totalExercisesPlayed);

        resetStaticPauseValues();


        //TODO  Ads
        //if facebook ad is avaliable then show it else show admob ad

        if(AdsManager.getInstance().isFacebookInterstitalLoaded())
        {
            AdsManager.getInstance().showFacebookInterstitialAd();
        }
        else
        {
            AdsManager.getInstance().showInterstitialAd();
        }
        //Todo AdsEnd
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
        if(!iscomplete)
        fragmentManager.beginTransaction().replace(R.id.fragment_container ,exerciseFragment,null).commit();
        else
            fragmentManager.beginTransaction().replace(R.id.fragment_container ,completeFragment , null).commit();
    }


    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    public  void  back()
    {
        Intent newActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(newActivity);
        finish();
//        onBackPressed();
    }

    public  void  PauseFragment(int remaingTime)
    {

        //TODO  Ads
        //if facebook ad is avaliable then show it else show admob ad

        if(AdsManager.getInstance().isFacebookInterstitalLoaded())
        {
            AdsManager.getInstance().showFacebookInterstitialAd();
        }
        else
        {
            AdsManager.getInstance().showInterstitialAd();
        }
        //Todo AdsEnd

        is_Paused = true;
        pauseTimer = remaingTime;

        fragmentManager.beginTransaction().replace(R.id.fragment_container ,pauseFragment,null).commit();
    }

    public  void  NextFragment()
    {
        restTime = exerciseDays.get(currentExercise).getDelay();
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
            TTSManager.getInstance(getApplication()).play("Take a Rest for " + restTime+"seconds" +"The Next Exercise is "+nextExerciseName) ;
        }
        else
        {
            if ( currentRound < exerciseDays.get(0).getRounds())
            {
                currentRound++;

                //TODO Analytics
                AnalyticsManager.getInstance().sendAnalytics("Round Complete", "Plan " + currentPlan + " Day " + currentDay);


                TTSManager.getInstance(getApplication()).play(" This is end of Round "+ currentRound +" Take a rest for" + restTime + "seconds . You have" + (exerciseDays.get(0).getRounds() - currentRound ) +"round remaining"  +"The Next Exercise is "+nextExerciseName);

                Log.i("1994:Current exercise" , "current rounds less than total");

                for (ExerciseDay exerciseDay: exerciseDays)
                {
                    exerciseDay.setStatus(false);
                }

                currentExercise = 0;
            }

        }

        //TODO round checking
        if ((currentRound < exerciseDays.get(0).getRounds() ))
        {

            Log.i("1994:Current round" , "Day not updated");

            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getExerciseComplete() + 1);

            exerciseDays.get(0).setRoundCompleted(currentRound);
        }
        else
        {
           // currentDay++;
            Log.i("1994:currentDay" , "Day Upgraded");

            LogHelper.logD("1994:Current Round" , "" + currentRound + "Get Rounds" + (exerciseDays.get(0).getRounds()-1));

//            Toast.makeText(this, "All exercise completed ", Toast.LENGTH_LONG).show();

            exerciseDays.get(0).setExerciseComplete(exerciseDays.get(0).getTotalExercise());

            exerciseDays.get(0).setRoundCompleted(currentRound);

            fragmentTransaction.add(R.id.fragment_container , completeFragment ,  null);

            //TODO Analytics
            AnalyticsManager.getInstance().sendAnalytics("Complete All Exercises", "Plan " + currentPlan + " Day " + currentDay);


            TTSManager.getInstance(getApplication()).play(" Well Done. This is end of day " + (currentDay+1) + "of your training");
            iscomplete = true;
            InsetData insetData = new InsetData();
            insetData.execute();
            return;

        }

        InsetData insetData = new InsetData();
        //Your Workout Today will consist of [5] different exercises completed in [3] rounds. You will do each exercise in short intense intervals followed by a rest of few seconds. You will get a rest of [60] seconds at the end of each round.
        LogHelper.logD("1994:Current round" , "" +currentRound);
        LogHelper.logD("1994:Currnet day" , ""  + currentDay);
        LogHelper.logD("1994:Current Exercise" ,"" + currentExercise);
        insetData.execute();

    }

    public  void  skipRestNextButtonClicked()
    {

    }

    @Override
    protected void onPause() {
        super.onPause();


        if(skipFragment !=null && skipFragment.isVisible())
            skipFragment.pauseOrRenume();
        else  if(exerciseFragment!=null && exerciseFragment.isVisible())
        {
            exerciseFragment.pause();
        }
        else if (nextFragment!=null && nextFragment.isVisible())
        {
            nextFragment.pauseOrRenume();
        }
        else
        {

        }
    }


    @SuppressLint("StaticFieldLeak")
    void  dbLoadData()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
                dataBase = AppDataBase.getInstance();
                exerciseDays = dataBase.exerciseDayDao().getExerciseDays(currentPlan, currentDay);


                //  currentExercise = AppStateManager.currentExercise;
                //currentRound = AppStateManager.roundCleared;

                totalRounds = exerciseDays.get(0).getRounds();
                totalExercises =  exerciseDays.get(0).getTotalExercise();

                totalExercisePerRound = exerciseDays.size();

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


                int exerciseId = exerciseDays.get(currentExercise).getId();
                exerciseName = dataBase.exerciseDao().findById(exerciseId).getName();
                displayName = dataBase.exerciseDao().findById(exerciseId).getDisplay_name();
                int time = exerciseDays.get(currentExercise).getReps();
              //  nextRest = exerciseDays.get(currentExercise).getDelay();

                LogHelper.logD("1994:" , "rest : " + restTime);


                currentReps = time;

                currentReps*=1000;

                if (currentExercise<exerciseDays.size()-1)
                {

                    nextExerciseName =  dataBase.exerciseDao().findById(exerciseDays.get(currentExercise+1).getId()).getDisplay_name();
                    nextExerciseImage = dataBase.exerciseDao().findById(exerciseDays.get(currentExercise+1).getId()).getName();
                }
                else
                {
                    nextExerciseName =  dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getDisplay_name();
                    nextExerciseImage =  dataBase.exerciseDao().findById(exerciseDays.get(0).getId()).getName();
                }

                LogHelper.logD("1994:Current round" , "" +currentRound);
                LogHelper.logD("1994:Currnet day" , ""  + currentDay);
                LogHelper.logD("1994:Current Exercise" ,"" + currentExercise);

                return null;
            }

            @Override
            protected void onPostExecute (Void aVoid){
                super.onPostExecute(aVoid);
                if(isCancelled())
                    return;
              //  StartSkipFragment();
//                initExerciseList();
            }

            @Override
            protected void onProgressUpdate (Void...values){
                super.onProgressUpdate(values);
            }


        }.execute();

    }




    public  class  InsetData extends AsyncTask<Void , Void ,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            dataBase.exerciseDayDao().insertAll(exerciseDays);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isCancelled())
                return;

            if(!iscomplete)
            dbLoadData();
        }
    }





}
