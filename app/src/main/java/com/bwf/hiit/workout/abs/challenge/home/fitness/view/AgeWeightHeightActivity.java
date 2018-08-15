package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.widgets.MyWheelPicker;

import java.util.ArrayList;
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
        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        user = new User();
        user.setId(1);
        setNumbers();
    }

    @OnClick({R.id.nextButtonNumberScreen, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nextButtonNumberScreen:
                user.setAge(numAge.getValue());
                float height = (float) ((numFeet.getValue() * 12) + numInches.getValue());
                user.setHeight(height);
                user.setWeight((float) numWeight.getValue());
                float bmi = (((float) numWeight.getValue()) / (height * height)) * 703;
                user.setBmi((int) bmi);
                startNewActivity();
                break;
            case R.id.btn_skip:
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

}
