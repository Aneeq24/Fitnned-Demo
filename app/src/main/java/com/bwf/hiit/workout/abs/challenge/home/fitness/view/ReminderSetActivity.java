package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderSetActivity extends AppCompatActivity {


    @BindView(R.id.edt_reminderTime)
    EditText edtReminderTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_set);
        ButterKnife.bind(this);
    }

    private void startActivity() {
        Intent newActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(newActivity);
        finish();
    }

    @OnClick({R.id.btn_setReminder, R.id.btn_skipReminder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setReminder:
                startActivity();
                break;
            case R.id.btn_skipReminder:
                startActivity();
                break;
        }
    }
}
