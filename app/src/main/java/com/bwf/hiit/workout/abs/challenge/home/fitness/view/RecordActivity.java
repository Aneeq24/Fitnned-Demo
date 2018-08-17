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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.MyMarkerView;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.RelativeRadioGroup;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.RecordViewModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {

    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    Toolbar toolbar;
    Context context;
    LineChart graph;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvMon;
    ImageView btnAddWeight;
    TextView tvBmi;
    RelativeLayout btnEditBmi;
    RelativeLayout btnMore;
    RelativeRadioGroup rgGraph;
    RecordViewModel mRecordViewModel;
    UserViewModel mUserViewModel;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        context = this;

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        toolbar = findViewById(R.id.toolbar10);
        graph = findViewById(R.id.graph);
        tvExerciseNo = findViewById(R.id.cf_exerciseNo);
        tvTotalTime = findViewById(R.id.cf_totalTime);
        tvKcal = findViewById(R.id.textView17);
        tvBmi = findViewById(R.id.tv_bmi);
        btnEditBmi = findViewById(R.id.btn_edit_bmi);
        btnMore = findViewById(R.id.btn_more);
        rgGraph = findViewById(R.id.rg_graph);
        tvMon = findViewById(R.id.tv_mon);
        btnAddWeight = findViewById(R.id.btn_add_weight);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        AnalyticsManager.getInstance().sendAnalytics("Recored_activity", "Recored_activity_started");

        toolbar.setNavigationOnClickListener(view1 -> finish());
        btnEditBmi.setOnClickListener(view12 -> showDialog());
        btnAddWeight.setOnClickListener(view12 -> showDialog());
        btnMore.setOnClickListener(view12 -> startActivity(new Intent(context, CalenderActivity.class)));

        rgGraph.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_lb_graph) {
                btnAddWeight.setVisibility(View.GONE);
                initApp(user);
                setKcalYAxis();
            } else if (i == R.id.rb_kg_graph) {
                setWeight();
                setWeightYAxis();
                btnAddWeight.setVisibility(View.VISIBLE);
                graph.invalidate();
            }
        });

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
                        bmi = ((weight) / (height * height)) * 703;
                    else {
                        bmi = (weight) / (height * height);
                        weight = weight * 2.20462f;
                    }
                    if (isCm)
                        height = height * 100 * 0.393701f;
                    tvBmi.setText(String.valueOf(math(bmi)) + bmiCategory(math(bmi)));
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

        edtWeight.setText(String.valueOf(math(user.getWeight() * 0.453592f)));
        edtCm.setText(String.valueOf(math(user.getHeight() * 2.54f)));

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
                edtWeight.setText(String.valueOf(math(user.getWeight() * 0.453592f)));
                edtCm.setText(String.valueOf(math(user.getHeight() * 2.54f)));
                rbKg.setChecked(true);
            } else if (i == R.id.rb_in) {
                edtFt.setVisibility(View.VISIBLE);
                edtIn.setVisibility(View.VISIBLE);
                edtCm.setVisibility(View.GONE);
                isCm = false;
                edtWeight.setText(String.valueOf(math(user.getWeight())));
                edtFt.setText(String.valueOf(math(user.getHeight() / 12)));
                edtIn.setText(String.valueOf(math(user.getHeight() % 12)));
                rbLbs.setChecked(true);
            }
        });
    }

    private void setDaysData() {
        RecyclerView rvHistory = findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(titles, date);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MMM");
        Date date = new Date();
        tvMon.setText(dateFormat.format(date));
    }

    @SuppressLint("SetTextI18n")
    private void initApp(User user) {
        tvBmi.setText(String.valueOf(user.getBmi()) + bmiCategory(user.getBmi()));
        tvExerciseNo.setText(String.valueOf(user.getTotalExcercise()) + "\nExercise");
        tvKcal.setText(String.valueOf(user.getTotalKcal()) + "\nKcal");
        tvTotalTime.setText(String.valueOf(user.getTotalTime()) + "\nMins");
        mRecordViewModel.getAllRecords().observe(this, records -> {
            if (records != null) {
                setupChart(records);
            }
        });
    }

    private void setupChart(List<Record> recordList) {
        graph.setDrawGridBackground(false);
        // no description text
        graph.getDescription().setEnabled(false);
        // enable touch gestures
        graph.setTouchEnabled(true);
        // enable scaling and dragging
        graph.setDragEnabled(true);
        graph.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        graph.setPinchZoom(true);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        setKcalYAxis();
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(graph); // For bounds control
        graph.setMarker(mv); // Set the marker to the chart
        XAxis xAxis = graph.getXAxis();
        xAxis.setAxisMaximum(30f);
        xAxis.setAxisMinimum(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        graph.getAxisRight().setEnabled(false);
        // add data
        setData(recordList);
        graph.animateX(500);
        // // dont forget to refresh the drawing
        graph.invalidate();
    }

    private void setKcalYAxis() {
        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(250f);
        leftAxis.setAxisMinimum(150f);
    }

    private void setWeightYAxis() {
        YAxis leftAxis = graph.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(user.getWeight() + 50f);
        leftAxis.setAxisMinimum(user.getWeight() - 50f);
    }

    private void setData(List<Record> recordList) {

        ArrayList<Entry> values = new ArrayList<>();
        if (recordList.size() == 0)
            values.add(new Entry(1, 1, getResources().getDrawable(R.drawable.star)));
        else {
            for (int i = 0; i < recordList.size(); i++)
                values.add(new Entry(recordList.get(i).getId() + 1, recordList.get(i).getWeight(), getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set;
        // create a dataset and give it a type
        set = new LineDataSet(values, "kcal");
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
        graph.setData(data);
        graph.getData().notifyDataChanged();
        graph.notifyDataSetChanged();

    }

    private void setWeight() {
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1, user.getWeight(), getResources().getDrawable(R.drawable.star)));

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
        graph.setData(data);
        graph.getData().notifyDataChanged();
        graph.notifyDataSetChanged();
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
        else return "";
    }

    private float convertIntoFloat(String xVal) {
        try {
            return Float.parseFloat(xVal);
        } catch (Exception ex) {
            return 0;
        }
    }

    public int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

}
