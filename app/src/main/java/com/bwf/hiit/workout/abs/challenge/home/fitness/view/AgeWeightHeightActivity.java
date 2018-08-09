package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.widgets.WheelWeightPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgeWeightHeightActivity extends AppCompatActivity {


    @BindView(R.id.num_age)
    WheelWeightPicker numAge;
    @BindView(R.id.num_feet)
    WheelWeightPicker numFeet;
    @BindView(R.id.num_inches)
    WheelWeightPicker numInches;
    @BindView(R.id.num_weight)
    WheelWeightPicker numWeight;

    User user;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_weight_height);
        ButterKnife.bind(this);
        user = new User();
        record = new Record();
        record.setDay(1);
        user.setId(1);
    }

    @OnClick({R.id.nextButtonNumberScreen, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextButtonNumberScreen:
                user.setAge(numAge.getCurrentWeight());
                record.setWeight(numWeight.getCurrentWeight());
                float height = (float) ((numFeet.getCurrentWeight() * 12) + numInches.getCurrentWeight());
                user.setHeight(height);
                new setUserGender().execute();
                startNewActivity();
                break;
            case R.id.btn_skip:
                startNewActivity();
                break;
        }
    }

    private void startNewActivity() {
        startActivity(new Intent(getApplicationContext(), ReminderSetActivity.class));
        finish();
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
                appDataBase.userdao().updatePlan(user);
            if (appDataBase != null)
                appDataBase.recorddao().insertAll(record);
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
