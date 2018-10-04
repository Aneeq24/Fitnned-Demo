package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AlarmManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderSetActivity extends AppCompatActivity {


    @BindView(R.id.num_hour)
    MyWheelPicker numHour;
    @BindView(R.id.num_min)
    MyWheelPicker numMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_set);
        ButterKnife.bind(this);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        setNumbers();
    }

    private void setNumbers(){
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i <= 24; i++)
            data.add(i);
        numHour.setData(data);
        data = new ArrayList<>();
        for (int i = 0; i <= 60; i++)
            data.add(i);
        numMin.setData(data);
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @OnClick({R.id.setbuttonReminderScreen, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setbuttonReminderScreen:
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, numHour.getValue());
                calSet.set(Calendar.MINUTE, numMin.getValue());
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);

                SharedPrefHelper.writeInteger(getApplicationContext(), getString(R.string.hour), numHour.getValue());
                SharedPrefHelper.writeInteger(getApplicationContext(), getString(R.string.minute), numMin.getValue());
                SharedPrefHelper.writeBoolean(getApplicationContext(), "reminder", true);
                AlarmManager.getInstance().setAlarm(this, calSet);
                startNewActivity();
                break;
            case R.id.btn_skip:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 19);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                SharedPrefHelper.writeInteger(getApplicationContext(), getString(R.string.hour), 7);
                SharedPrefHelper.writeInteger(getApplicationContext(), getString(R.string.minute), 0);
                SharedPrefHelper.writeBoolean(getApplicationContext(), "reminder", true);
                AlarmManager.getInstance().setAlarm(this, cal);
                AnalyticsManager.getInstance().sendAnalytics("skip_set_reminder", "skip_set_reminder");
                startNewActivity();
                break;
        }
    }

}
