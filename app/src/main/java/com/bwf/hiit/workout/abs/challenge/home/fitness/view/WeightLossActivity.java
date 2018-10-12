package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeightLossActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.num_age)
    MyWheelPicker numAge;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.num_weight)
    MyWheelPicker numWeight;
    @BindView(R.id.rg_weight)
    RadioGroup rgWeight;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.num_feet)
    MyWheelPicker numFeet;
    @BindView(R.id.num_inches)
    MyWheelPicker numInches;
    @BindView(R.id.ly_inches)
    LinearLayout lyInches;
    @BindView(R.id.rg_height)
    RadioGroup rgHeight;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tv_result)
    TextView tvResult;

    boolean isKg = true;
    boolean isCm = false;
    boolean ifMale = true;
    int activityLevel;
    List<Integer> data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_loss);
        ButterKnife.bind(this);
        String[] activities = getResources().getStringArray(R.array.activity_list);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, activities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        setNumbers();

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_male:
                    ifMale = true;
                    break;
                case R.id.rb_in:
                    ifMale = false;
                    break;
            }
        });

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
        setAge();
        setKg();
        setFeet();
        setInches();
    }

    private void setAge() {
        data = new ArrayList<>();
        for (int i = 10; i <= 50; i++)
            data.add(i);
        numAge.setData(data);
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

    @OnClick({R.id.btn_back, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_calculate:
                getLossWeight();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void getLossWeight() {
        double height, inches, feet;
        double activityMultiplier;
        double age = numAge.getValue();
        double mass = numWeight.getValue();

        if (isCm)
            height = numFeet.getValue() / 100;
        else {
            inches = numInches.getValue();
            feet = numFeet.getValue();
            height = (feet * 12) + inches;
        }


        switch (activityLevel) {
            case 0:
                activityMultiplier = 1.2;
                break;
            case 1:
                activityMultiplier = 1.375;
                break;
            case 2:
                activityMultiplier = 1.550;
                break;
            case 3:
                activityMultiplier = 1.725;
                break;
            case 4:
                activityMultiplier = 1.9;
                break;
            default:
                activityMultiplier = 1.2;
                break;
        }

        double bmrDouble;
        if (ifMale) {
            bmrDouble = 88.362 + (13.3 * mass) + (4.7 * height) - (5.677 * age);
        } else {
            bmrDouble = 447.593 + (9.2 * mass) + (3.098 * height) - (4.3 * age);
        }

        double tddeDouble = bmrDouble * activityMultiplier;

        tvResult.setText("You can lose " + math(tddeDouble) + "lbs in 1 week");

    }

    public int math(double f) {
        return (int) Math.floor(f);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityLevel = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
