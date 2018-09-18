package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class MainActivity extends AppCompatActivity {

    int age;
    int height;
    int neck;
    int waist;
    int hip;
    double mass;
    int activityLevel = 0;
    boolean male = true;
    boolean female = false;

    public static final int MAX_AGE = 60;
    public static final int MAX_HEIGHT = 200;
    public static final int MAX_MASS = 300;

    EditText ageInput;
    EditText heightInput;
    EditText massInput;
    EditText neckInput;
    EditText waistInput;
    EditText hipInput;

    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_male:
                male = true;
                female = false;
                break;
            case R.id.rb_female:
                female = true;
                male = false;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner mySpinner = findViewById(R.id.activity_input);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.activity_list));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter((myAdapter));
        mySpinner.setSelection(0);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activityLevel = mySpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onCalculate(View view) {
        try {
            ageInput = findViewById(R.id.age_input);
            heightInput = findViewById(R.id.height_input);
            massInput = findViewById(R.id.mass_input);
            neckInput = findViewById(R.id.neck_input);
            waistInput = findViewById(R.id.waist_input);
            hipInput = findViewById(R.id.hip_input);
            if (isEmpty(ageInput) && isEmpty(heightInput) && isEmpty(massInput) &&
                    !massInput.toString().equals(".") ) {
                age = Integer.parseInt(ageInput.getText().toString());
                height = Integer.parseInt(heightInput.getText().toString());
                neck = Integer.parseInt(neckInput.getText().toString());
                waist = Integer.parseInt(waistInput.getText().toString());
                hip = Integer.parseInt(hipInput.getText().toString());
                Intent passdata_intent = new Intent(this, secondActivity.class);
                mass = Double.parseDouble(massInput.getText().toString());
                passdata_intent.putExtra("mass", mass);
                passdata_intent.putExtra("activityLevel", activityLevel);
                passdata_intent.putExtra("height", height);
                passdata_intent.putExtra("age", age);
                passdata_intent.putExtra("neck", neck);
                passdata_intent.putExtra("waist", waist);
                passdata_intent.putExtra("hip", hip);
                if (male) {
                    passdata_intent.putExtra("ifMale", true);
                } else {
                    passdata_intent.putExtra("ifMale", false);
                }
                if ((age <= MAX_AGE) && (age >= 0) && (height <= MAX_HEIGHT) && mass < MAX_MASS) {
                    startActivity(passdata_intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Figures Seem Incorrect, Please Enter Valid Figures.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Complete The Necessary Sections Correctly", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please Complete The Necessary Sections Correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() != 0;
    }
}
