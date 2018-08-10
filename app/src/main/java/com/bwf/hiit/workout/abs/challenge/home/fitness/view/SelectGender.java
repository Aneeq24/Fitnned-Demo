package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.google.android.gms.ads.AdView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGender extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ButterKnife.bind(this);
        user = new User();
        user.setId(1);
        AnalyticsManager.getInstance().sendAnalytics("gender_screen_started", "activity_started");

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);
    }


    private void setGender(String gender, int sex) {
        user.setGender(sex);
        SharedPrefHelper.writeString(getApplicationContext(), "gender", gender);
        AnalyticsManager.getInstance().sendAnalytics("gender_selected", gender);
        LogHelper.logD("1994:", "" + gender);
        new setUserGender().execute();
        startNewActivity();
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), AgeWeightHeightActivity.class));
        finish();
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
                startNewActivity();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class setUserGender extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            AppDataBase appDataBase = AppDataBase.getInstance();

            if (appDataBase != null)
                appDataBase.userdao().updateUser(user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
