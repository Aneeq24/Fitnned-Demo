package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.AppStateManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;

public class DayProgressList extends AppCompatActivity {



    RecyclerView recyclerView;

    public DataModelWorkout dataModelsWorkout;


    int plan;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_progress_list);

        dataModelsWorkout = new DataModelWorkout();
        populateData();
        initView();


        Intent intent = getIntent();

        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan),0);


        TTSManager.getInstance(getApplication()).play("You have selected plan " + AppStateManager.mainCategory);
    }

    void  populateData() {

       //TODO
        switch (AppStateManager.mainCategory-1)
        {
            case  0:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
//                        new String[]
//                        {
//                                "Day 1", "Day 2", "Day 3",
//                                "Day 4","Day 5", "Day 6" ,
//                                "Day 7" ,"Day 8" , "Day 9",
//                                "Day 10" ,"Day 11" , "Day 12",
//                                "Day 13" ,"Day 14" , "Day 15",
//                                "Day 16" ,"Day 17" , "Day 18",
//                                "Day 19" ,"Day 20" , "Day 21",
//                                "Day 22" ,"Day 23" , "Day 24",
//                                "Day 25" ,"Day 26" , "Day 27",
//                                "Day 28" ,"Day 29" , "Day 30",
//
//                        };
                    for (int i = 0; i <30 ; i++)
                    {
                        dataModelsWorkout.progress.add(i, (float) 0);

                    }

                break;
            case  1:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
//                        new String[]
//                        {
//                                "Day 1", "Day 2", "Day 3",
//                                "Day 4","Day 5", "Day 6" ,
//                                "Day 7" ,"Day 8" , "Day 9",
//                                "Day 10" ,"Day 11" , "Day 12",
//                                "Day 13" ,"Day 14" , "Day 15",
//                                "Day 16" ,"Day 17" , "Day 18",
//                                "Day 19" ,"Day 20" , "Day 21",
//                                "Day 22" ,"Day 23" , "Day 24",
//                                "Day 25" ,"Day 26" , "Day 27",
//                                "Day 28" ,"Day 29" , "Day 30",
//                        };
                for (int i = 0; i <30 ; i++)
                {
                    dataModelsWorkout.progress.add(i, (float) 0.0);

                }

                break;
            case  2:


                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
//                new String[]
//                        {
//                                "Day 1", "Day 2", "Day 3",
//                                "Day 4","Day 5", "Day 6" ,
//                                "Day 7" ,"Day 8" , "Day 9",
//                                "Day 10" ,"Day 11" , "Day 12",
//                                "Day 13" ,"Day 14" , "Day 15",
//                                "Day 16" ,"Day 17" , "Day 18",
//                                "Day 19" ,"Day 20" , "Day 21",
//                                "Day 22" ,"Day 23" , "Day 24",
//                                "Day 25" ,"Day 26" , "Day 27",
//                                "Day 28" ,"Day 29" , "Day 30",
//
//                        };
                for (int i = 0; i <30 ; i++)
                {
                    dataModelsWorkout.progress.add(i, (float) 0);
                }

                break;

                default:

        }
    }

    void initView()
    {
       // RecyclerView recycleViewActivity =  findViewById(R.id.dayTaskRecycleid);
       // recycleViewActivity.setLayoutManager(new LinearLayoutManager(this));
       // recycleViewActivity.setAdapter(new DayRecycleAdapter(dataModelsWorkout));
    }
    @Override
    public void onBackPressed()
    {
        // resetData();
        super.onBackPressed();
    }

    void  resetData()
    {
        dataModelsWorkout.dayName = null;
        dataModelsWorkout.iconForExcersice = null;
        dataModelsWorkout.progress = null;
    }


}
