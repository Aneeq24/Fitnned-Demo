package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGender extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ButterKnife.bind(this);
    }

    private void setGender(String gender) {
        LogHelper.logD("1994:", "" + gender);
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), AgeWeightHeightActivity.class));
        finish();
    }

    @OnClick({R.id.iv_female, R.id.tv_female, R.id.nextgenderButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_male:
                setGender("Male");
                break;
            case R.id.tv_female:
                setGender("FEMALE");
                break;
            case R.id.nextgenderButton:
                startNewActivity();
                break;
        }
    }
}
