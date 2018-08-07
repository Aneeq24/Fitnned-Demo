package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bwf.hiit.workout.abs.challenge.home.fitness.AppStateManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;

public class DayProgressList extends AppCompatActivity {

    DataModelWorkout dataModelsWorkout;
    int plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_progress_list);

        dataModelsWorkout = new DataModelWorkout();
        populateData();
        initView();

        Intent intent = getIntent();
        plan = intent.getIntExtra(getApplicationContext().getString(R.string.plan), 0);
        TTSManager.getInstance(getApplication()).play("You have selected plan " + AppStateManager.mainCategory);
    }

    private void populateData() {

        switch (AppStateManager.mainCategory - 1) {
            case 0:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);

                for (int i = 0; i < 30; i++) {
                    dataModelsWorkout.progress.add(i, (float) 0);

                }

                break;
            case 1:
                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
                for (int i = 0; i < 30; i++) {
                    dataModelsWorkout.progress.add(i, (float) 0.0);

                }

                break;
            case 2:


                dataModelsWorkout.dayName = getResources().getStringArray(R.array.days_list);
                for (int i = 0; i < 30; i++) {
                    dataModelsWorkout.progress.add(i, (float) 0);
                }

                break;

            default:

        }
    }

    private void initView() {
        // RecyclerView recycleViewActivity =  findViewById(R.id.dayTaskRecycleid);
        // recycleViewActivity.setLayoutManager(new LinearLayoutManager(this));
        // recycleViewActivity.setAdapter(new DayRecycleAdapter(dataModelsWorkout));
    }

    @Override
    public void onBackPressed() {
        resetData();
        super.onBackPressed();
    }

    private void resetData() {
        dataModelsWorkout.dayName = null;
        dataModelsWorkout.iconForExcersice = null;
        dataModelsWorkout.progress = null;
    }


}
