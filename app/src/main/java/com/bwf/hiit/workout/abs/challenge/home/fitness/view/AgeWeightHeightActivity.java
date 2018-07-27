package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class AgeWeightHeightActivity extends AppCompatActivity {



    TextView ageText;
    TextView feetText;
    TextView inches;
    TextView weight;

    Button button;

    TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_weight_height);



        ageText = findViewById(R.id.agetextInput);
        feetText = findViewById(R.id.feetTextInput);
        inches  = findViewById(R.id.inchesTextInput);
        button = findViewById(R.id.nextButtonNumberScreen);


        button.setOnClickListener(view -> startNewActivity());
        skipText = findViewById(R.id.skipButtonNoScreen);


    }

    void  startNewActivity()
    {
        Intent newActivity = new Intent(getApplicationContext(), ReminderSetActivity.class);
        startActivity(newActivity);
        finish();
    }


    void  showText()
    {

    }
}
