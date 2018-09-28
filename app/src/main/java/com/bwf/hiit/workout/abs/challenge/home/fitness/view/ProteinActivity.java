package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProteinActivity extends AppCompatActivity {

    @BindView(R.id.num_age)
    MyWheelPicker numAge;
    @BindView(R.id.num_weight)
    MyWheelPicker numWeight;
    @BindView(R.id.num_feet)
    MyWheelPicker numFeet;
    @BindView(R.id.num_inches)
    MyWheelPicker numInches;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;
    @BindView(R.id.tv_result)
    TextView tvResult;

    double protienLowerDouble;
    double protienUpperDouble;
    double mass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protien);
        ButterKnife.bind(this);

        setNumbers();
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

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.btn_back, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_calculate:
                mass = numWeight.getValue();
                protienLowerDouble = (((mass / 2.2) * 0.8));
                protienUpperDouble = (((mass / 2.2) * 1.7));
                tvResult.setText("REQUIRED PROTIEN IS " + (int) protienLowerDouble +
                        " - " + (int) protienUpperDouble + " TO GRAMS");
                lyResult.setVisibility(View.VISIBLE);
                break;
        }
    }
}
