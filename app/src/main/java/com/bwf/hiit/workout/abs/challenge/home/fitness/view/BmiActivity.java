package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyWheelPicker;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BmiActivity extends AppCompatActivity {

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

    boolean isKg = true;
    boolean isCm = false;
    List<Integer> data;
    @BindView(R.id.img_needle)
    ImageView imgNeedle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        ButterKnife.bind(this);

        AnalyticsManager.getInstance().sendAnalytics("BmiActivity", "BmiActivity");

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
                    isCm = true;
                    tvHeight.setText("cm");
                    lyInches.setVisibility(View.GONE);
                    break;
                case R.id.rb_in:
                    setFeet();
                    isCm = false;
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

    @OnClick({R.id.btn_back, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_calculate:
                calculateBmi();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateBmi() {
        float weight, height, inches, feet, bmi;

        weight = numWeight.getValue();

        if (isCm)
            height = numFeet.getValue() / 100;
        else {
            inches = numInches.getValue();
            feet = numFeet.getValue();
            height = (feet * 12) + inches;
        }

        if (isCm && !isKg) {
            height = height * 100 * 0.393701f;
        }

        if (isKg && !isCm) {
            weight = weight * 2.20462f;
            isKg = false;
        }

        bmi = (weight) / (height * height);

        if (!isKg) {
            bmi *= 703;
            isKg = true;
        }

        tvResult.setText("Your BMI " + math(bmi) + bmiCategory(Integer.parseInt(mathRound(bmi))));

        setImage(bmi);
    }

    public String math(float f) {
        return String.valueOf((int) Math.floor(f));
    }

    public String mathRound(float f) {
        return String.valueOf(Math.round(f));
    }

    private String bmiCategory(int bmi) {
        if (bmi > 0 && bmi < 19)
            return " - Under Weight";
        else if (bmi >= 19 && bmi < 25)
            return " - Healthy Weight";
        else if (bmi >= 25 && bmi < 30)
            return " - Over Weight";
        else if (bmi >= 30)
            return " - Heavily Over Weight";
        else return "";
    }

    private void setImage(float bmi) {
        int angle = 0;
        if (bmi > 0 && bmi <= 18.5) {
            angle = 18;
        } else if (bmi > 18.5 && bmi <= 24.9) {
            angle = 45;
        } else if (bmi > 24.9 && bmi <= 29.9) {
            angle = 90;
        } else if (bmi > 29.9 && bmi <= 34.9) {
            angle = 130;
        } else if (bmi > 34.9) {
            angle = 180;
        }

        imgNeedle.setPivotX(imgNeedle.getWidth() / 2);

        RotateAnimation rotateAnimation1 = new RotateAnimation(0, angle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation1.setDuration(4000);

        imgNeedle.startAnimation(rotateAnimation1);
    }

}
