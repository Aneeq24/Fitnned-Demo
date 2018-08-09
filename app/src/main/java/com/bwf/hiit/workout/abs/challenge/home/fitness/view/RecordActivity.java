package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.CompleteFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordActivity extends AppCompatActivity {


    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    Toolbar toolbar;
    Context context;
    GraphView graph;
    RecyclerView rvHistory;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvBmi;
    ImageView btnEditBmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);

        context = this;

        toolbar = findViewById(R.id.toolbar10);
        graph = findViewById(R.id.graph);
        rvHistory = findViewById(R.id.rv_days);
        tvExerciseNo = findViewById(R.id.cf_exerciseNo);
        tvTotalTime = findViewById(R.id.cf_totalTime);
        tvKcal = findViewById(R.id.textView17);
        tvBmi = findViewById(R.id.tv_bmi);
        btnEditBmi = findViewById(R.id.btn_edit_bmi);

        AnalyticsManager.getInstance().sendAnalytics("Recored_activity", "Recored_activity_started");

        setDaysData();

        toolbar.setNavigationOnClickListener(view1 -> finish());

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        if (!SharedPrefHelper.readBoolean(context, "rate"))
            Utils.setRateAppDialog(context);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(20, 60),
                new DataPoint(21, 62),
                new DataPoint(22, 59),
                new DataPoint(23, 66),
                new DataPoint(24, 63)
        });

        graph.getViewport().setScrollableY(true);
        graph.getViewport().setBackgroundColor(Color.WHITE);
        graph.getViewport().setBorderColor(R.color.white);

        // enable scaling and scrolling
        graph.setCursorMode(true);
        series.setColor(Color.BLUE);
        graph.addSeries(series);

        btnEditBmi.setOnClickListener(view12 -> showDialog());
    }

    private void setDaysData() {
        DayAdapter mAdapter = new DayAdapter(titles, date);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_more)
    public void onViewClicked() {
        startActivity(new Intent(context, CalenderActivity.class));
    }

    EditText edtWeight;
    EditText edtCm;
    EditText edtFt;
    EditText edtIn;
    RadioGroup rgWeight;
    RadioGroup rgHeight;
    TextView btnCancel;
    TextView btnSave;

    float weight, height, inches, feet, centimeter;
    boolean isKg = true;
    boolean isCm = true;
    float bmi;

    @SuppressLint("SetTextI18n")
    public void showDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("BMI Calculater");
        dialog.setContentView(R.layout.dialog_bmi);

        edtWeight = dialog.findViewById(R.id.edt_weight);
        edtCm = dialog.findViewById(R.id.edt_cm);
        edtFt = dialog.findViewById(R.id.edt_ft);
        edtIn = dialog.findViewById(R.id.edt_in);
        rgWeight = dialog.findViewById(R.id.rg_weight);
        rgHeight = dialog.findViewById(R.id.rg_height);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnSave = dialog.findViewById(R.id.btn_save);

        rgWeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_lb) {
                edtWeight.setHint("00.00 LB");
                isKg = false;
            } else if (i == R.id.rb_kg) {
                edtWeight.setHint("00.00 KG");
                isKg = true;
            }
        });

        rgHeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_cm) {
                edtCm.setVisibility(View.VISIBLE);
                edtFt.setVisibility(View.GONE);
                edtIn.setVisibility(View.GONE);
                isCm = true;
            } else if (i == R.id.rb_in) {
                edtFt.setVisibility(View.VISIBLE);
                edtIn.setVisibility(View.VISIBLE);
                edtCm.setVisibility(View.GONE);
                isCm = false;
            }
        });

        btnSave.setOnClickListener(view -> {

            weight = Integer.parseInt(edtWeight.getText().toString().trim());

            if (isCm) {
                centimeter = Integer.parseInt(edtCm.getText().toString().trim());
                height = centimeter / 100;
            } else {
                inches = Float.parseFloat(edtIn.getText().toString().trim());
                feet = Float.parseFloat(edtFt.getText().toString().trim());
                height = (feet * 12) + inches;
            }

            if (!isKg)
                weight = weight * Float.parseFloat("0.453592");

            bmi = (weight) / (height * height);

            SharedPrefHelper.writeInteger(context, "bmi", (int) bmi);
            tvBmi.setText(String.valueOf(bmi));
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
