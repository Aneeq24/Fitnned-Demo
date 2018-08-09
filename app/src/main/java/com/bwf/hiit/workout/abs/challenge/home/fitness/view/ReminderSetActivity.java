package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AlarmManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.widgets.WheelWeightPicker;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderSetActivity extends AppCompatActivity {


    Calendar reminderDateTime = Calendar.getInstance();
    @BindView(R.id.num_hour)
    WheelWeightPicker numHour;
    @BindView(R.id.num_min)
    WheelWeightPicker numMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_set);
        ButterKnife.bind(this);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @OnClick({R.id.setbuttonReminderScreen, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setbuttonReminderScreen:
                reminderDateTime.set(Calendar.HOUR_OF_DAY, numHour.getCurrentWeight());
                reminderDateTime.set(Calendar.MINUTE, numMin.getCurrentWeight());
                reminderDateTime.set(Calendar.SECOND, 0);
                SharedPrefHelper.writeBoolean(getApplicationContext(), "reminder", true);
                AlarmManager.getInstance().setAlarm(this, reminderDateTime);
                startNewActivity();
                break;
            case R.id.btn_skip:
                startNewActivity();
                break;
        }
    }

}
