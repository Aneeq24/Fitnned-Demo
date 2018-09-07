package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.RecordViewModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class RecordActivity extends AppCompatActivity {

    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    Toolbar toolbar;
    Context context;
    LineChart graphWeight;
    BarChart graphKcal;
    TextView tvExerciseNo;
    TextView tvTotalMin;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvMon;
    TextView tvMonKcal;
    TextView tvBmi;
    RelativeLayout btnEditBmi;
    RelativeLayout btnMore;
    RecordViewModel mRecordViewModel;
    UserViewModel mUserViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        context = this;
        AdsManager.getInstance().showFacebookInterstitialAd();

        tvMon = findViewById(R.id.tv_mon);
        tvBmi = findViewById(R.id.tv_bmi);
        tvKcal = findViewById(R.id.tv_kcal);
        toolbar = findViewById(R.id.toolbar);
        btnMore = findViewById(R.id.btn_more);
        tvTotalMin = findViewById(R.id.tv_time);
        tvTotalTime = findViewById(R.id.tv_mins);
        graphKcal = findViewById(R.id.graph_kcal);
        tvMonKcal = findViewById(R.id.tv_mon_kcal);
        btnEditBmi = findViewById(R.id.btn_edit_bmi);
        tvExerciseNo = findViewById(R.id.tv_exercise);
        graphWeight = findViewById(R.id.graph_weight);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        AnalyticsManager.getInstance().sendAnalytics("Recored_activity", "Recored_activity_started");

        toolbar.setNavigationOnClickListener(view1 -> finish());
        btnEditBmi.setOnClickListener(view12 -> showDialog());
        btnMore.setOnClickListener(view12 -> startActivity(new Intent(context, CalenderActivity.class)));

        mUserViewModel.getUser().observe(this, user -> {
            if (user != null) {
                this.user = user;
                initApp(user);
            }
        });
        setDaysData();
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

    float weight, height, inches, feet, bmi;
    boolean isKg = true;
    boolean isCm = true;

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        isKg = true;
        isCm = true;
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("BMI Calculator")
                .customView(R.layout.dialog_bmi, true)
                .positiveText("Save")
                .onPositive((dialog1, which) -> {
                    weight = convertIntoFloat(edtWeight.getText().toString().trim());

                    if (isCm)
                        height = convertIntoFloat(edtCm.getText().toString().trim()) / 100;
                    else {
                        inches = convertIntoFloat(edtIn.getText().toString().trim());
                        feet = convertIntoFloat(edtFt.getText().toString().trim());
                        height = (feet * 12) + inches;
                    }
                    if (!isKg)
                        weight = user.getWeight();

                    bmi = (weight) / (height * height);

                    if (!isKg)
                        bmi *= 703;

                    if (isKg)
                        weight = weight * 2.20462f;
                    if (isCm)
                        height = height * 100 * 0.393701f;

                    tvBmi.setText(math(bmi) + bmiCategory(Integer.parseInt(mathround(bmi))));
                    user.setWeight(weight);
                    user.setHeight(height);
                    user.setBmi((int) bmi);
                    mUserViewModel.update(user);
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

        edtWeight.setText(mathround(user.getWeight() * 0.453592f));
        edtCm.setText(mathround(user.getHeight() * 2.54f));
        edtFt.setText(math(user.getHeight() / 12));
        edtIn.setText(mathround(user.getHeight() % 12));

        rgWeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_lb) {
                isKg = false;
                isCm = false;
                edtWeight.setHint("00.00 LB");
                edtWeight.setText(math(user.getWeight()));
            } else if (i == R.id.rb_kg) {
                isKg = true;
                isCm = true;
                edtWeight.setHint("00.00 KG");
                edtWeight.setText(mathround(user.getWeight() * 0.453592f));
            }
        });

        rgHeight.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_cm) {
                isKg = true;
                isCm = true;
                edtFt.setVisibility(View.GONE);
                edtIn.setVisibility(View.GONE);
                edtCm.setVisibility(View.VISIBLE);
                edtCm.setText(mathround(user.getHeight() * 2.54f));
            } else if (i == R.id.rb_in) {
                isKg = false;
                isCm = false;
                edtCm.setVisibility(View.GONE);
                edtFt.setVisibility(View.VISIBLE);
                edtIn.setVisibility(View.VISIBLE);
                edtFt.setText(math(user.getHeight() / 12));
                edtIn.setText(mathround(user.getHeight() % 12));
            }
        });
    }

    private void setDaysData() {
        RecyclerView rvHistory = findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(days);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MMM");
        Date date = new Date();
        tvMon.setText(dateFormat.format(date));
        tvMonKcal.setText(dateFormat.format(date));
    }

    @SuppressLint("SetTextI18n")
    private void initApp(User user) {
        tvBmi.setText(String.valueOf(user.getBmi()) + bmiCategory(user.getBmi()));
        tvExerciseNo.setText(String.valueOf(user.getTotalExcercise()) + " Exercise");
        tvKcal.setText(String.valueOf(user.getTotalKcal()) + " Kcal");
        tvTotalMin.setText(String.valueOf(user.getTotalTime()));
        tvTotalTime.setText(String.valueOf(user.getTotalTime()) + " Mins");
        setupWeightChart();
        mRecordViewModel.getAllRecords().observe(this, records -> {
            if (records != null) {
                setupKcalChart(records);
            }
        });
    }

    private void setupWeightChart() {
        graphWeight.setDrawGridBackground(false);
        // no description text
        graphWeight.getDescription().setEnabled(false);
        // enable touch gestures
        graphWeight.setTouchEnabled(false);
        // enable scaling and dragging
        graphWeight.setDragEnabled(false);
        graphWeight.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        graphWeight.setPinchZoom(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(graphWeight); // For bounds control
//        graphWeight.setMarker(mv); // Set the marker to the chart
        XAxis xAxis = graphWeight.getXAxis();
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(31);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.TRANSPARENT);
        graphWeight.getAxisRight().setEnabled(false);
        // add data
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(getCurrentDay(), user.getWeight()));
        YAxis leftAxis = graphWeight.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(user.getWeight() + 50f);
        leftAxis.setAxisMinimum(user.getWeight() - 50f);
        LineDataSet set;
        // create a dataset and give it a type
        set = new LineDataSet(values, "lbs");
        set.setDrawIcons(false);
        // set the line to be drawn like this "- - - - - -"
        set.enableDashedLine(10f, 0f, 0f);
        set.enableDashedHighlightLine(10f, 0f, 0f);
        set.setColor(Color.parseColor("#00aeef"));
        set.setCircleColor(Color.parseColor("#00aeef"));
        set.setLineWidth(1f);
        set.setValueTextColor(Color.parseColor("#00aeef"));
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_green);
            set.setFillDrawable(drawable);
        } else {
            set.setFillColor(Color.parseColor("#00aeef"));
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        // set data
        graphWeight.setData(data);
        graphWeight.getData().notifyDataChanged();
        graphWeight.notifyDataSetChanged();
        graphWeight.animateX(500);
        // // dont forget to refresh the drawing
        graphWeight.invalidate();
    }

    private void setupKcalChart(List<Record> recordList) {
        graphKcal.setDrawGridBackground(false);
        // no description text
        graphKcal.getDescription().setEnabled(false);
        // enable touch gestures
        graphKcal.setTouchEnabled(false);
        // enable scaling and dragging
        graphKcal.setDragEnabled(false);
        graphKcal.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        graphKcal.setPinchZoom(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(graphWeight); // For bounds control
//        graphWeight.setMarker(mv); // Set the marker to the chart
        XAxis xAxis = graphKcal.getXAxis();
        xAxis.setAxisMinimum(getCurrentDay() - 3);
        xAxis.setAxisMaximum(getCurrentDay() + 3);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.TRANSPARENT);
        graphKcal.getAxisRight().setEnabled(false);

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Record> mList = new ArrayList<>();
        if (recordList.size() == 0)
            values.add(new BarEntry(1, 1));
        else {
            for (int i = 0; i < recordList.size(); i++) {
                if (getCurrentDay() == Integer.parseInt(recordList.get(i).getDay()))
                    mList.add(recordList.get(i));
                else
                    values.add(new BarEntry(Integer.parseInt(recordList.get(i).getDay()), recordList.get(i).getKcal()));
            }
            float sum = 0;
            for (int i = 0; i < mList.size(); i++) {
                sum = sum + mList.get(i).getKcal();
            }
            values.add(new BarEntry(getCurrentDay(), sum));
        }
        YAxis leftAxis = graphKcal.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(leftAxis.getAxisMaximum() + 50f);
        leftAxis.setAxisMinimum(0);
        BarDataSet set;
        // create a dataset and give it a type
        set = new BarDataSet(values, "kcal");
        set.setDrawIcons(false);
        // set the line to be drawn like this "- - - - - -"
        set.setColor(Color.parseColor("#FF671C"));

        set.setValueTextColor(Color.parseColor("#FF671C"));
        set.setValueTextSize(9f);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the datasets
        // create a data object with the datasets
        BarData data = new BarData(dataSets);
        // set data
        graphKcal.getAxisLeft().setAxisMaximum(data.getYMax() + 100);
        graphKcal.setData(data);
        graphKcal.getData().notifyDataChanged();
        graphKcal.notifyDataSetChanged();
        graphKcal.animateX(500);
        // // dont forget to refresh the drawing
        graphKcal.invalidate();
    }

    private int getCurrentDay() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
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

    private float convertIntoFloat(String xVal) {
        try {
            return Float.parseFloat(xVal);
        } catch (Exception ex) {
            return 0;
        }
    }

    public String math(float f) {
        return String.valueOf((int) Math.floor(f));
    }

    public String mathround(float f) {
        return String.valueOf(Math.round(f));
    }

}
