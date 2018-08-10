package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.ArrayList;
import java.util.List;

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

    List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        recordList = new ArrayList<>();
        context = this;

        toolbar = findViewById(R.id.toolbar10);
        graph = findViewById(R.id.graph);
        rvHistory = findViewById(R.id.rv_days);
        tvExerciseNo = findViewById(R.id.cf_exerciseNo);
        tvTotalTime = findViewById(R.id.cf_totalTime);
        tvKcal = findViewById(R.id.textView17);
        tvBmi = findViewById(R.id.tv_bmi);
        btnEditBmi = findViewById(R.id.btn_edit_bmi);

        tvBmi.setText(String.valueOf(SharedPrefHelper.readInteger(context, "bmi")));

        AnalyticsManager.getInstance().sendAnalytics("Recored_activity", "Recored_activity_started");

        setDaysData();

        toolbar.setNavigationOnClickListener(view1 -> finish());

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        btnEditBmi.setOnClickListener(view12 -> showDialog());

        new getUserRecords().execute();
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
    RadioButton rbCm;
    RadioButton rbIn;

    float weight, height, inches, feet;
    boolean isKg = true;
    boolean isCm = true;
    float bmi;

    @SuppressLint("SetTextI18n")
    public void showDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("BMI Calculator")
                .customView(R.layout.dialog_bmi, true)
                .positiveText("Save")
                .onPositive((dialog1, which) -> {
                    weight = Integer.parseInt(edtWeight.getText().toString().trim());

                    if (isCm)
                        height = 100 * Integer.parseInt(edtCm.getText().toString().trim());
                    else {
                        inches = Float.parseFloat(edtIn.getText().toString().trim());
                        feet = Float.parseFloat(edtFt.getText().toString().trim());
                        height = (feet * 12) + inches;
                    }

                    if (!isKg)
                        bmi = ((weight) / (height * height)) * 703;
                    else
                        bmi = (weight) / (height * height);

                    SharedPrefHelper.writeInteger(context, "bmi", (int) bmi);
                    tvBmi.setText(String.valueOf((int) bmi));
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
        rbIn= view.findViewById(R.id.rb_in);

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
            } else if (i == R.id.rb_in) {
                edtFt.setVisibility(View.VISIBLE);
                edtIn.setVisibility(View.VISIBLE);
                edtCm.setVisibility(View.GONE);
                isCm = false;
            }
        });

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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            initApp();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void initApp() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < recordList.size(); i++) {
            series.appendData(new DataPoint(recordList.get(i).getDay(), recordList.get(i).getWeight()), true, 30, false);
        }
        series.setColor(Color.BLUE);
        graph.addSeries(series);
        graph.setCursorMode(true);
    }
}
