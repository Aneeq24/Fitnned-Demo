package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class ReminderSetActivity extends AppCompatActivity {


    EditText reminderTime ;

    Button setReminderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_set);

        reminderTime = findViewById(R.id.reminderScreenTime);

        setReminderTime  = findViewById(R.id.setbuttonReminderScreen);

        setReminderTime.setOnClickListener(view -> startActivity());

    }


    void  startActivity()
    {
        Intent newActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(newActivity);
        finish();
    }
}
