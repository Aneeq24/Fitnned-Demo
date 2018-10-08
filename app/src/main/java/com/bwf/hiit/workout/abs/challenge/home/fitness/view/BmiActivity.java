package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BmiActivity extends AppCompatActivity {

    float[] bmiRange = {0, 18.5f, 24.9f, 29.9f, 34.9f, 35};

    @BindView(R.id.num_weight)
    MyWheelPicker numWeight;
    @BindView(R.id.rg_weight)
    RadioGroup rgWeight;
    @BindView(R.id.num_feet)
    MyWheelPicker numFeet;
    @BindView(R.id.num_inches)
    MyWheelPicker numInches;
    @BindView(R.id.rg_height)
    RadioGroup rgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        ButterKnife.bind(this);

        setNumbers();
    }

    private void setNumbers() {
        List<Integer> data;
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


    @OnClick({R.id.btn_back, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_calculate:
                break;
        }
    }

    private void calculateBmi() {

    }

}
