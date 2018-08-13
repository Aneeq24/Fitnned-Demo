package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {

    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    Toolbar toolbar;
    Context context;
    GraphView graph;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvBmi;
    RelativeLayout btnEditBmi;
    RelativeLayout btnMore;
    List<Record> recordList;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        recordList = new ArrayList<>();
        context = this;

        toolbar = findViewById(R.id.toolbar10);
        graph = findViewById(R.id.graph);
        tvExerciseNo = findViewById(R.id.cf_exerciseNo);
        tvTotalTime = findViewById(R.id.cf_totalTime);
        tvKcal = findViewById(R.id.textView17);
        tvBmi = findViewById(R.id.tv_bmi);
        btnEditBmi = findViewById(R.id.btn_edit_bmi);
        btnMore = findViewById(R.id.btn_more);

        AnalyticsManager.getInstance().sendAnalytics("Recored_activity", "Recored_activity_started");

        setDaysData();

        toolbar.setNavigationOnClickListener(view1 -> finish());

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        btnEditBmi.setOnClickListener(view12 -> showDialog());
        btnMore.setOnClickListener(view12 -> startActivity(new Intent(context, CalenderActivity.class)));

        new getUserRecords().execute();
    }

    private void setDaysData() {
        RecyclerView rvHistory = findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(titles, date);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
    }

    EditText edtWeight;
    EditText edtCm;
    EditText edtFt;
    EditText edtIn;
    RadioGroup rgWeight;
    RadioGroup rgHeight;
    RadioButton rbCm;
    RadioButton rbIn;
    RadioButton rbKg;
    RadioButton rbLbs;

    float weight, height, inches, feet;
    boolean isKg = true;
    boolean isCm = true;
    float bmi;

    @SuppressLint("SetTextI18n")
    private void showDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("BMI Calculator")
                .customView(R.layout.dialog_bmi, true)
                .positiveText("Save")
                .onPositive((dialog1, which) -> {
                    weight = convertIntoInteger(edtWeight.getText().toString().trim());

                    if (isCm)
                        height = (float) convertIntoInteger(edtCm.getText().toString().trim()) / 100;
                    else {
                        inches = convertIntoFloat(edtIn.getText().toString().trim());
                        feet = convertIntoFloat(edtFt.getText().toString().trim());
                        height = (feet * 12) + inches;
                    }

                    if (!isKg)
                        bmi = ((weight) / (height * height)) * 703;
                    else
                        bmi = (weight) / (height * height);

                    SharedPrefHelper.writeInteger(context, "bmi", (int) bmi);
                    tvBmi.setText(String.valueOf((int) bmi) + bmiCategory((int) bmi));
                    dialog1.dismiss();
                })
                .negativeText("Cancel")
                .onNegative((dialog12, which) -> dialog12.dismiss())
                .show();

        View view = dialog.getCustomView();

        assert view != null;
        edtWeight = view.findViewById(R.id.edt_weight);
        edtCm = view.findViewById(R.id.edt_cm);
        edtFt = view.findViewById(R.id.edt_ft);
        edtIn = view.findViewById(R.id.edt_in);
        rgWeight = view.findViewById(R.id.rg_weight);
        rgHeight = view.findViewById(R.id.rg_height);
        rbCm = view.findViewById(R.id.rb_cm);
        rbIn = view.findViewById(R.id.rb_in);
        rbKg = view.findViewById(R.id.rb_kg);
        rbLbs = view.findViewById(R.id.rb_lb);

        edtWeight.setText(String.valueOf((int) (user.getWeight() * 0.453592)));
        edtCm.setText(String.valueOf((int) (user.getHeight() * 2.54)));

        rgWeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_lb) {
                edtWeight.setHint("00.00 LB");
                isKg = false;
                rbIn.setChecked(true);
            } else if (i == R.id.rb_kg) {
                edtWeight.setHint("00.00 KG");
                isKg = true;
                rbCm.setChecked(true);
            }
        });

        rgHeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_cm) {
                edtCm.setVisibility(View.VISIBLE);
                edtFt.setVisibility(View.GONE);
                edtIn.setVisibility(View.GONE);
                isCm = true;
                edtWeight.setText(String.valueOf((int) (user.getWeight() * 0.453592)));
                edtCm.setText(String.valueOf((int) (user.getHeight() * 2.54)));
                rbKg.setChecked(true);
            } else if (i == R.id.rb_in) {
                edtFt.setVisibility(View.VISIBLE);
                edtIn.setVisibility(View.VISIBLE);
                edtCm.setVisibility(View.GONE);
                isCm = false;
                edtWeight.setText(String.valueOf((int) user.getWeight()));
                edtFt.setText(String.valueOf((int) (user.getHeight() / 12)));
                edtIn.setText(String.valueOf((int) (user.getHeight() % 12)));
                rbLbs.setChecked(true);
            }
        });

    }

    private String bmiCategory(int bmi) {
        if (bmi > 0 && bmi < 19)
            return " - Under Weight";
        else if (bmi >= 19 && bmi < 25)
            return " - Healthy Weight";
        else if (bmi >= 25 && bmi < 30)
            return " - Over Weight";
        else if (bmi > 30)
            return " - Heavily Over Weight";
        else return null;
    }

    private int convertIntoInteger(String xVal) {
        try {
            return Integer.parseInt(xVal);
        } catch (Exception ex) {
            return 0;
        }
    }

    private float convertIntoFloat(String xVal) {
        try {
            return Float.parseFloat(xVal);
        } catch (Exception ex) {
            return 0;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getUserRecords extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recordList = AppDataBase.getInstance().recorddao().getAllRecords();
            user = AppDataBase.getInstance().userdao().findById(1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initApp();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void initApp() {
        float weight = user.getWeight();
        float height = user.getHeight();
        float bmi = ((weight) / (height * height)) * 703;
        tvBmi.setText(String.valueOf((int) bmi) + bmiCategory((int) bmi));
        tvExerciseNo.setText(String.valueOf(user.getTotalExcercise()) + "\nExercise");
        tvKcal.setText(String.valueOf(user.getTotalKcal()) + "\nKcal");
        tvTotalTime.setText(String.valueOf(user.getTotalTime()) + "\nMins");

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < recordList.size(); i++) {
            series.appendData(new DataPoint(recordList.get(i).getId() + 1, recordList.get(i).getWeight()), true, 5, false);
        }

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Aug");
        graph.getGridLabelRenderer().setVerticalAxisTitle("lbs");
        graph.getGridLabelRenderer().setPadding(1);
        graph.getGridLabelRenderer().setGridColor(getColor(R.color.colorDarkGray));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getColor(R.color.colorDarkGray));
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(getColor(R.color.colorDarkGray));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getColor(R.color.colorDarkGray));
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(getColor(R.color.colorDarkGray));

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(255);
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(31);

        series.setColor(Color.BLUE);
        graph.addSeries(series);
        graph.setCursorMode(true);

    }
}
