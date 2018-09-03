package com.bwf.hiit.workout.abs.challenge.home.fitness.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.google.android.gms.ads.AdView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGender extends AppCompatActivity {

    UserViewModel mViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ButterKnife.bind(this);
        AnalyticsManager.getInstance().sendAnalytics("gender_screen_started", "activity_started");

        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mViewModel.getUser().observe(this,user ->
        this.user = user);

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);
    }


    @OnClick({R.id.rb_male, R.id.rb_female, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_male:
                setGender("MALE", 1);
                break;
            case R.id.rb_female:
                setGender("FEMALE", 0);
                break;
            case R.id.btn_skip:
                AnalyticsManager.getInstance().sendAnalytics("skip_gender", "skip_gender");
                startNewActivity();
                break;
        }
    }

    private void setGender(String gender, int sex) {
        user.setGender(sex);
        AnalyticsManager.getInstance().sendAnalytics("gender_selected", gender);
        LogHelper.logD("1994:", "" + gender);
        mViewModel.update(user);
        startNewActivity();
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), ReminderSetActivity.class));
        finish();
    }
}
