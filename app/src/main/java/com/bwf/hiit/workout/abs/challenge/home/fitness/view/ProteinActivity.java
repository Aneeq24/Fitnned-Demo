package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProteinActivity extends AppCompatActivity {

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
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.ly_inches)
    LinearLayout lyInches;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;

    boolean isKg = true;
    List<Integer> data;
    double proteinLowerDouble;
    double proteinUpperDouble;
    double mass;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protien);
        ButterKnife.bind(this);

        setNumbers();

        rgWeight.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_kg:
                    isKg = true;
                    setKg();
                    tvWeight.setText("kg");
                    break;
                case R.id.rb_lb:
                    setLbs();
                    isKg = false;
                    tvWeight.setText("lbs");
                    break;
            }
        });

        rgHeight.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_cm:
                    setCm();
                    tvHeight.setText("cm");
                    lyInches.setVisibility(View.GONE);
                    break;
                case R.id.rb_in:
                    setFeet();
                    tvHeight.setText("ft");
                    lyInches.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }

    private void setNumbers() {
        setKg();
        setFeet();
        setInches();
    }

    private void setKg() {
        data = new ArrayList<>();
        for (int i = 40; i <= 200; i++)
            data.add(i);
        numWeight.setData(data);
    }

    private void setLbs() {
        data = new ArrayList<>();
        for (int i = 100; i <= 300; i++)
            data.add(i);
        numWeight.setData(data);
    }

    private void setFeet() {
        data = new ArrayList<>();
        for (int i = 5; i <= 10; i++)
            data.add(i);
        numFeet.setData(data);
    }

    private void setInches() {
        data = new ArrayList<>();
        for (int i = 0; i <= 12; i++)
            data.add(i);
        numInches.setData(data);
    }

    private void setCm() {
        data = new ArrayList<>();
        for (int i = 100; i <= 200; i++)
            data.add(i);
        numFeet.setData(data);
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
                proteinLowerDouble = (((mass / 2.2) * 0.8));
                proteinUpperDouble = (((mass / 2.2) * 1.7));
                tvResult.setText("REQUIRED PROTEIN IS\n" + (int) proteinLowerDouble +
                        " - " + (int) proteinUpperDouble + " TO GRAMS");
                lyResult.setVisibility(View.VISIBLE);
                break;
        }
    }
}
