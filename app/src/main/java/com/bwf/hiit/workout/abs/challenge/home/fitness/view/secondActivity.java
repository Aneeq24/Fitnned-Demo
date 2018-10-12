package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class secondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        boolean ifMale = getIntent().getExtras().getBoolean("ifMale");
        int age = getIntent().getExtras().getInt("age");
        int height = getIntent().getExtras().getInt("height");
        int neck = getIntent().getExtras().getInt("neck");
        int waist = getIntent().getExtras().getInt("waist");
        int hip = getIntent().getExtras().getInt("hip");
        double mass = getIntent().getExtras().getDouble("mass");
        int activityLevel = getIntent().getExtras().getInt(("activityLevel"));
        double activityMultiplier;
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
        TextView bmrResultText = findViewById(R.id.bmrResult);
        TextView tddeResultText = findViewById(R.id.tddeResult);
        TextView bfpResultText = findViewById(R.id.bfpResult);
        TextView proteinResultText = findViewById(R.id.proteinResult);
        //Mifflin = (10.m + 6.25h - 5.0a) + s
        //m is mass in kg, h is height in cm, a is age in years, s is +5 for males and -151 for females
        double bmrDouble;
        double bfpDouble;
        double protienDouble;
        if (ifMale) {
            //Men: BMR = 88.362 + (13.3 x weight in kg) + (4.7 x height in cm) - (5.677 x age in years)
            //BFP = 86.010×log10(abdomen-neck) - 70.041×log10(height) + 36.76
            //BFP = 495 / (1.0324 - 0.19077×log10(waist-neck) ) + 0.15456×log10(height))- 450
            bmrDouble = 88.362 + (13.3 * mass) + (4.7 * height) - (5.677 * age);
            bfpDouble = (495 / ((1.0324 - 0.19077 * Math.log10(waist - neck)) + (0.15456 * Math.log10(height)))) - 450;
        } else {
            //Women: BMR = 447.593 + (9.2 x weight in kg) + (3.098  x height in cm) - (4.3 x age in years)
            //BFP = 163.205×log10(waist+hip-neck) - 97.684×(log10(height)) + 36.76
            //BFP = 495 / (1.29579 - 0.35004×log10(waist+hip-neck) + 0.22100×log10(height))- 450
            bmrDouble = 447.593 + (9.2 * mass) + (3.098 * height) - (4.3 * age);
            bfpDouble = (495 / ((1.29579 - 0.35004 * Math.log10(waist + hip - neck)) + (0.22100 * Math.log10(height)))) - 450;
        }
        protienDouble = ((((mass * 2.20462) / 2.2) * 0.8));
        int bmr = (int) bmrDouble;
        double tddeDouble = bmrDouble * activityMultiplier;
        int bfp = (int) bfpDouble;
        bmrResultText.setText(String.valueOf(bmr));
        tddeResultText.setText(String.valueOf(tddeDouble));
        bfpResultText.setText(String.valueOf(bfp));
        proteinResultText.setText(String.valueOf(protienDouble));
    }
}
