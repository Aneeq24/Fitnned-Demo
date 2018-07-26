package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.AppStateManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DailyExerciseAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.List;



public class DailyExerciseInfo extends AppCompatActivity {


    public DataModelWorkout dataModelWorkout= new DataModelWorkout();

    ImageView startButton;
    Toolbar toolbar;

    int plan = 0;
    int day = 0;

    SharedPreferences sharedPreferences;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily_exercise_info);

        //AppStateManager.currentExercise = 0;
        toolbar =  findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        com.google.android.gms.ads.AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);


        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan),0);
        day = intent.getIntExtra(getApplicationContext().getString(R.string.day_selected) , 0);
        AnalyticsManager.getInstance().sendAnalytics("Activity Started", "Exercise List Activity");
        sharedPreferences = this.getSharedPreferences(String.valueOf(getApplicationContext()), Context.MODE_PRIVATE);

        dataModelWorkout = new DataModelWorkout();

        populateData();

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              Intent i = new Intent(view.getContext() , PlayingExercise.class);
                i.putExtra(view.getContext().getString(R.string.day_selected) , day);
                i.putExtra(view.getContext().getString(R.string.plan) , plan);

              view.getContext().startActivity(i);

            }
        });

       // TTSManager.getInstance(getApplication()).play("You have selected day " + AppStateManager.dailyExercise_ExeciseIndex);

    }

    void  initExerciseList()
    {
        RecyclerView recyleView = findViewById(R.id.dailyExercise_RecyclerView);
        recyleView.setLayoutManager(new LinearLayoutManager(this));
        recyleView.setAdapter(new  DailyExerciseAdapter(dataModelWorkout));

        validatingDb();

    }

    void  validatingDb()
    {
        int totalRounds = exerciseDays.get(0).getRounds();
        int totalExercises =  exerciseDays.get(0).getTotalExercise();

        int roundsCleared = exerciseDays.get(0).getRoundCompleted();
        int totalexercisePlayed =exerciseDays.get(0).getRoundCompleted();
        int currentExercise = 0;


        for (ExerciseDay day: exerciseDays)
        {
            if (day.isStatus())
                currentExercise++;
        }

        AppStateManager.currentExercise  = currentExercise;
        AppStateManager.roundCleared = roundsCleared;




    }





    List<ExerciseDay> exerciseDays;
    @SuppressLint("StaticFieldLeak")
    void  downLoaddbData()
    {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDataBase dataBase = AppDataBase.getInstance();
                exerciseDays = dataBase.exerciseDayDao().getExerciseDays(plan, day);
                for (ExerciseDay day : exerciseDays)
                {
                    Exercise exercise = dataBase.exerciseDao().findById(day.getId());
                    dataModelWorkout.dailyExercise_ExerciseName.add(exercise.getDisplay_name());
                    dataModelWorkout.dailyExercise_ImageIndex.add(R.drawable.pause_screen_next_bar_image);


                }
                return null;
            }

                @Override
                protected void onPostExecute (Void aVoid){
                    super.onPostExecute(aVoid);
                    initExerciseList();
                }

                @Override
                protected void onProgressUpdate (Void...values){
                    super.onProgressUpdate(values);
                }


        }.execute();
//
//        try {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    AppDataBase dataBase = AppDataBase.getInstance();
//
//                    List<ExerciseDay> exerciseDays = dataBase.exerciseDayDao().getExerciseDays(AppStateManager.mainCategory, AppStateManager.dailyExercise_ExeciseIndex);
//
//                    for (ExerciseDay day : exerciseDays) {
//
//                                +day.getReps() + "pid" + day.getId());
//                        Exercise exercise = dataBase.exerciseDao().findById(day.getId());
//
//
//                        dataModelWorkout.dailyExercise_ExerciseName.add(exercise.getName());
//                        dataModelWorkout.dailyExercise_ImageIndex.add(R.drawable.pause_screen_next_bar_image);
//                    }
//
//                }
//
//
//            });
//            thread.setPriority(Thread.MIN_PRIORITY);
//            thread.start();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally {
//
//           // initExerciseList();
//        }

    }

    void  populateData()
    {

        downLoaddbData();

//        dataModelWorkout.dailyExercise_ExerciseName = new  String[]{
//                "Exercise 1",
//                "Exercise 2",
//                "Exercise 3",
//                "Exercise 4",
//                "Exercise 5",
//                "Exercise 6",
//                "Exercise 7",
//                "Exercise 8",
//                "Exercise 9",
//        };
//
//        dataModelWorkout.dailyExercise_ImageIndex = new  int[]
//                {
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//                    R.drawable.pause_screen_next_bar_image,
//
//                };
    }




}
