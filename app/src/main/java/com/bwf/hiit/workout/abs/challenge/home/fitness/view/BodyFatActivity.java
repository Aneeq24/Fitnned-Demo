package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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

public class BodyFatActivity extends AppCompatActivity {

    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
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
    @BindView(R.id.num_age)
    MyWheelPicker numAge;
    @BindView(R.id.img_under)
    ImageView imgUnder;
    @BindView(R.id.img_normal)
    ImageView imgNormal;
    @BindView(R.id.img_over)
    ImageView imgOver;
    @BindView(R.id.img_obese)
    ImageView imgObese;
    @BindView(R.id.img_high_over)
    ImageView imgHighOver;

    boolean isKg = true;
    boolean isCm = false;
    boolean ifMale = true;
    List<Integer> data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat);
        ButterKnife.bind(this);

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

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.btn_back, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_calculate:
                double bfpDouble;
                int bmi = getBmi();
                if (numAge.getValue() < 18) {
                    if (ifMale) {
                        bfpDouble = 1.51 * bmi - 0.7 * numAge.getValue() - 2.2;
                    } else {
                        bfpDouble = 1.51 * bmi - 0.7 * numAge.getValue() + 1.4;
                    }
                } else {
                    if (ifMale) {
                        bfpDouble = 1.20 * bmi + 0.23 * numAge.getValue() - 16.2;
                    } else {
                        bfpDouble = 1.20 * bmi + 0.23 * numAge.getValue() - 5.4;
                    }
                }
                setImage(bmi);
                tvResult.setText("Body Fat: " + mathRound(bfpDouble) + "%");
        }
    }

    private int getBmi() {
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

        return math(bmi);
    }

    public int math(float f) {
        return (int) Math.floor(f);
    }

    public String mathRound(double f) {
        return String.valueOf(Math.round(f));
    }

    private void setImage(float bmi) {
        if (bmi > 0 && bmi <= 18.5) {
            imgUnder.setImageResource(R.drawable.bmi_s_underweight);
            imgNormal.setImageResource(R.drawable.bmi_n_normal);
            imgOver.setImageResource(R.drawable.bmi_n_overwight);
            imgObese.setImageResource(R.drawable.bmi_n_obese);
            imgHighOver.setImageResource(R.drawable.bmi_n_extremly_obese);
        } else if (bmi > 18.5 && bmi <= 24.9) {
            imgUnder.setImageResource(R.drawable.bmi_n_underweight);
            imgNormal.setImageResource(R.drawable.bmi_s_normal);
            imgOver.setImageResource(R.drawable.bmi_n_overwight);
            imgObese.setImageResource(R.drawable.bmi_n_obese);
            imgHighOver.setImageResource(R.drawable.bmi_n_extremly_obese);
        } else if (bmi > 24.9 && bmi <= 29.9) {
            imgUnder.setImageResource(R.drawable.bmi_n_underweight);
            imgNormal.setImageResource(R.drawable.bmi_n_normal);
            imgOver.setImageResource(R.drawable.bmi_s_overwight);
            imgObese.setImageResource(R.drawable.bmi_n_obese);
            imgHighOver.setImageResource(R.drawable.bmi_n_extremly_obese);
        } else if (bmi > 29.9 && bmi <= 34.9) {
            imgUnder.setImageResource(R.drawable.bmi_n_underweight);
            imgNormal.setImageResource(R.drawable.bmi_n_normal);
            imgOver.setImageResource(R.drawable.bmi_n_overwight);
            imgObese.setImageResource(R.drawable.bmi_s_obese);
            imgHighOver.setImageResource(R.drawable.bmi_n_extremly_obese);
        } else if (bmi > 34.9) {
            imgUnder.setImageResource(R.drawable.bmi_n_underweight);
            imgNormal.setImageResource(R.drawable.bmi_n_normal);
            imgOver.setImageResource(R.drawable.bmi_n_overwight);
            imgObese.setImageResource(R.drawable.bmi_n_obese);
            imgHighOver.setImageResource(R.drawable.bmi_s_extremly_obese);
        }
    }

}
