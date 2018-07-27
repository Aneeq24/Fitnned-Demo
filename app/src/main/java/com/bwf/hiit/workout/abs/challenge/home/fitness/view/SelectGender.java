package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;

public class SelectGender extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);


        ImageView male = findViewById(R.id.iv_male);

        male.setOnClickListener(view ->
                setGender("Male"));

        ImageView female = findViewById(R.id.iv_female);
        female.setOnClickListener(view -> setGender("FEMALE"));

        Button nextButton = findViewById(R.id.nextgenderButton);

        nextButton.setOnClickListener(view -> startNewActivity() );
    }


    void  setGender(String gender)
    {
        LogHelper.logD("1994:" , "" + gender);
    }

    void  startNewActivity()
    {
        Intent newActivity = new Intent(getApplicationContext(), AgeWeightHeightActivity.class);
        startActivity(newActivity);
        finish();
    }
}
