package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ReminderSetActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Objects;

public class CompleteFragment extends Fragment {
    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    Toolbar toolbar;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvBmi;
    ImageView btnEditBmi;
    ImageView btnAddReminder;
    Context context;
    GraphView graph;
    PlayingExercise playingExercise;
    RecyclerView rvHistory;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        toolbar = view.findViewById(R.id.toolbar10);
        tvExerciseNo = view.findViewById(R.id.cf_exerciseNo);
        tvTotalTime = view.findViewById(R.id.cf_totalTime);
        tvKcal = view.findViewById(R.id.textView17);
        tvBmi = view.findViewById(R.id.tv_bmi);
        btnEditBmi = view.findViewById(R.id.btn_edit_bmi);
        graph = view.findViewById(R.id.graph);
        rvHistory = view.findViewById(R.id.rv_days);
        btnAddReminder = view.findViewById(R.id.btn_add_reminder);
        context = getContext();

        playingExercise = (PlayingExercise) getActivity();
        assert playingExercise != null;

        TTSManager.getInstance(getActivity().getApplication()).play(" Well Done. This is end of day " + (playingExercise.currentDay + 1) + "of your training");
        AnalyticsManager.getInstance().sendAnalytics("workout_complete", "day " + (playingExercise.currentDay + 1));
        int minutes = (playingExercise.totaTimeSpend % 3600) / 60;

        playingExercise.exerciseDays.get(playingExercise.currentExercise).setStatus(true);
        playingExercise.exerciseDays.get(playingExercise.currentExercise).setTotalKcal(SharedPrefHelper.readInteger(context, "kcal"));
        @SuppressLint("DefaultLocale") String timeString = String.format("%02d", minutes);

        tvExerciseNo.setText(" " + playingExercise.totalExercisesPlayed + "\nExercise");
        tvTotalTime.setText(" " + timeString + "\nMins");

        if (SharedPrefHelper.readInteger(context, "bmi") != 0)
            tvBmi.setText(String.valueOf(SharedPrefHelper.readInteger(context, "bmi")));

        String kcal = String.valueOf(playingExercise.exerciseDays.get(playingExercise.currentExercise).getTotalKcal());
        tvKcal.setText(kcal + "\nKCAL");
        SharedPrefHelper.writeInteger(context, "kcal", 0);

        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CompleteFragment.this).commit();
                getActivity().finish();
            }
        });

        btnEditBmi.setOnClickListener(view12 -> showDialog());

        btnAddReminder.setOnClickListener(view12 -> startActivity(new Intent(context, ReminderSetActivity.class)));

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

        setDaysData();

        return view;
    }

    private void setDaysData() {
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
    TextView btnCancel;
    TextView btnSave;

    float weight, height, inches, feet, centimeter;
    boolean isKg = true;
    boolean isCm = true;
    float bmi;

    @SuppressLint("SetTextI18n")
    public void showDialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
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
                height = (feet * Float.parseFloat("30.48")) + inches;
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
