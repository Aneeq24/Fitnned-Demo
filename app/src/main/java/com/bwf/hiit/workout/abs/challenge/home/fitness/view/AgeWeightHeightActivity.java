package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgeWeightHeightActivity extends AppCompatActivity {

    @BindView(R.id.num_age)
    MyWheelPicker numAge;
    @BindView(R.id.num_feet)
    MyWheelPicker numFeet;
    @BindView(R.id.num_inches)
    MyWheelPicker numInches;
    @BindView(R.id.num_weight)
    MyWheelPicker numWeight;
    UserViewModel mViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_weight_height);
        ButterKnife.bind(this);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mViewModel.getUser().observe(this, user ->
                this.user = user);

        setNumbers();
    }

    @OnClick({R.id.nextButtonNumberScreen, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextButtonNumberScreen:
                user.setAge(numAge.getValue());
                int height = ((numFeet.getValue() * 12) + numInches.getValue());
                user.setHeight(height);
                SharedPrefHelper.writeInteger(getApplicationContext(), "weight", numWeight.getValue());
                user.setWeight(numWeight.getValue());
                float bmi = (((float) numWeight.getValue()) / (height * height)) * 703;
                user.setBmi((int) bmi);
                startNewActivity();
                break;
            case R.id.btn_skip:
                AnalyticsManager.getInstance().sendAnalytics("skip_age_weight", "skip_age_weight");
                startNewActivity();
                break;
        }
    }

    private void setNumbers() {
        List<Integer> data = new ArrayList<>();
        for (int i = 20; i <= 70; i++)
            data.add(i);
        numAge.setData(data);
        data = new ArrayList<>();
        for (int i = 5; i <= 10; i++)
            data.add(i);
        numFeet.setData(data);
        data = new ArrayList<>();
        for (int i = 0; i <= 12; i++)
            data.add(i);
        numInches.setData(data);
        data = new ArrayList<>();
        for (int i = 100; i <= 200; i++)
            data.add(i);
        numWeight.setData(data);
    }

    private void startNewActivity() {
        mViewModel.update(user);
        startActivity(new Intent(getApplicationContext(), ReminderSetActivity.class));
        finish();
    }

    private String getCurrentDay() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("d");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
