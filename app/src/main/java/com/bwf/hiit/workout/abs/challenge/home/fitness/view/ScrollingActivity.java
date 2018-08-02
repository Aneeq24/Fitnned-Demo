package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayRecycleAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.dinuscxj.progressbar.CircleProgressBar;

public class ScrollingActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    public DataModelWorkout dataModelsWorkout;

    CircleProgressBar circleProgressBarLeft;
    CircleProgressBar  circleProgressBarCompleted;

    DayRecycleAdapter dayRecycleAdapter;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    int plan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        Intent intent = getIntent();

        com.google.android.gms.ads.AdView adVie = findViewById(R.id.banner_day);
        com.google.android.gms.ads.AdView adView = findViewById(R.id.baner_Admob);

        AdsManager.getInstance().showBanner(adVie);
        adVie.setAlpha(0);
        AdsManager.getInstance().showBanner(adView);

        circleProgressBarLeft = findViewById(R.id.line_progress_left);
        circleProgressBarCompleted = findViewById(R.id.line_progress_finished);
        circleProgressBarLeft.setProgressFormatter((progress, max) -> progress + "");

        //TODO Analytics
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "day_selection_activity");

        if(getIntent() != null && intent.getExtras() != null && intent.hasExtra(getString(R.string.plan))){
            plan = getIntent().getIntExtra(getString(R.string.plan), 0);
        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        dataModelsWorkout = new DataModelWorkout();
        populateData();
        initView();


        String planName = "";
        int i = plan-1;
        switch (i)
        {
            case 0:
                planName = "Beginner";
                break;
            case 1:
                planName = "Intermediate";
                break;
            case 2:
                planName = "Advanced";
                break;
                default:
                    break;
        }

//        TTSManager.getInstance(getApplication()).play("You have selected plan " + planName + "  Mode of 30 Day Ab Challenge");


    }


    int val = 0;
        @SuppressLint("StaticFieldLeak")
        void  populateData() {



        dataModelsWorkout.curretPlan  = plan;
            //TODO
            switch (plan-1)
            {
                case  0:
                    dataModelsWorkout.dayName =  getResources().getStringArray(R.array.days_list);  //new String[]

                         for ( int i = 0; i<30 ; i++  )
                         {
                             dataModelsWorkout.progress.add(i , (float)0);
                         }

                    break;
                case  1:
                    dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);//new String[]


                    for ( int i = 0; i<30 ; i++  )
                    {
                        dataModelsWorkout.progress.add(i , (float)0);
                    }

                    break;
                case  2:
                    dataModelsWorkout.dayName =getResources().getStringArray(R.array.days_list);


                    for ( int i = 0; i<30 ; i++  )
                    {
                        dataModelsWorkout.progress.add(i , (float)0);
                    }

                    break;

                default:

            }


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    AppDataBase dataBase = AppDataBase.getInstance();

                    for(int i =0 ; i< 30 ; i++)
                    {
                        int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan,
                                i+1).get(0).getExerciseComplete();
                        int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan,
                                i+1).get(0).getTotalExercise();


                        float v = (float) totalComplete/(float) totalExercises;

                        LogHelper.logD("1994:",""+ v);
                       if(v>=1)
                       {
                            val++;
                             LogHelper.logD("1994:",""+ val);
                       }

                    }

                    int dayLeft = 30 - val;

                    circleProgressBarLeft.setMax(30);
                    circleProgressBarLeft.setProgress(dayLeft);
                    LogHelper.logD("1993" , "Day left" + (dayLeft));
                    circleProgressBarCompleted.setMax(30);
                    circleProgressBarCompleted.setProgress(val);

                    return null;
                }

                @Override
                protected void onPostExecute (Void aVoid){
                    super.onPostExecute(aVoid);
                    if(isCancelled())
                        return;

                }

                @Override
                protected void onProgressUpdate (Void...values){
                    super.onProgressUpdate(values);
                }

            }.execute();
        }

        void initView()
        {
            RecyclerView recycleViewActivity =  findViewById(R.id.dayTaskRecycleid);
            recycleViewActivity.setNestedScrollingEnabled(false);
            recycleViewActivity.setLayoutManager(new LinearLayoutManager(this));
            dayRecycleAdapter = new DayRecycleAdapter(ScrollingActivity.this , dataModelsWorkout);
            recycleViewActivity.setAdapter(dayRecycleAdapter);
        }
        @Override
        public void onBackPressed()
        {
            super.onBackPressed();

        }


        boolean paused;

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    protected void onResume()
    {
        super.onResume();
        if(paused)
           dayRecycleAdapter.resetAdapter(dataModelsWorkout);

    }

    void  resetData()
   {
            dataModelsWorkout.dayName = null;
            dataModelsWorkout.iconForExcersice = null;
            dataModelsWorkout.progress = null;
   }





}
