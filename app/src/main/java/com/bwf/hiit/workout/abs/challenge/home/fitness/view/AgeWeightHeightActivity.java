package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class AgeWeightHeightActivity extends AppCompatActivity {

    Button btnNext;
    TextView inches;
    TextView ageText;
    TextView feetText;
    TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_weight_height);

        ageText = findViewById(R.id.agetextInput);
        feetText = findViewById(R.id.feetTextInput);
        inches  = findViewById(R.id.inchesTextInput);
        btnNext = findViewById(R.id.nextButtonNumberScreen);

        btnNext.setOnClickListener(view -> startNewActivity());
        skipText = findViewById(R.id.skipButtonNoScreen);
    }

    private void  startNewActivity() {
        startActivity(new Intent(getApplicationContext(), ReminderSetActivity.class));
        finish();
    }

}
