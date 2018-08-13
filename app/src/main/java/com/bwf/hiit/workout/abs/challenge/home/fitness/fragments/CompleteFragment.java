package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.CalenderActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ConfirmReminderActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class CompleteFragment extends Fragment {

    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

    private static TextView tvTime;

    Toolbar toolbar;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    TextView tvBmi;
    RelativeLayout btnEditBmi;
    RelativeLayout btnAddReminder;
    RelativeLayout btnMore;
    Context context;
    GraphView graph;
    PlayingExercise playingExercise;
    Record record;
    List<Record> recordList;
    User user, updateUser;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        recordList = new ArrayList<>();
        toolbar = view.findViewById(R.id.toolbar10);
        tvExerciseNo = view.findViewById(R.id.cf_exerciseNo);
        tvTotalTime = view.findViewById(R.id.cf_totalTime);
        tvKcal = view.findViewById(R.id.textView17);
        tvBmi = view.findViewById(R.id.tv_bmi);
        btnEditBmi = view.findViewById(R.id.btn_edit_bmi);
        graph = view.findViewById(R.id.graph);
        btnAddReminder = view.findViewById(R.id.btn_add_reminder);
        btnMore = view.findViewById(R.id.btn_more);
        tvTime = view.findViewById(R.id.tv_reminder);

        context = getContext();
        record = new Record();
        updateUser = new User();

        if (!SharedPrefHelper.readBoolean(context, "rate")) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    TTSManager.getInstance(playingExercise.getApplication()).play("If You Like Our Workout App Please Do Rate Us At The End Of This Workout");
                }
            }, 5000);
        }

        playingExercise = (PlayingExercise) getActivity();
        assert playingExercise != null;

        TTSManager.getInstance(getActivity().getApplication()).play(" Well Done. This is end of day " + playingExercise.currentDay + "of your training");
        AnalyticsManager.getInstance().sendAnalytics("day " + playingExercise.currentDay,"workout_complete");

        playingExercise.exerciseDays.get(playingExercise.currentExercise).setTotalKcal(SharedPrefHelper.readInteger(context, "kcal"));
        playingExercise.exerciseDays.get(playingExercise.currentExercise).setStatus(true);

        int minutes = (playingExercise.totaTimeSpend % 3600) / 60;
        @SuppressLint("DefaultLocale") String timeString = String.format("%02d", minutes);
        tvTotalTime.setText(timeString);

        record.setWeight(playingExercise.exerciseDays.get(playingExercise.currentExercise).getTotalKcal());
        record.setType(getPlanName());

        tvKcal.setText(String.valueOf(playingExercise.exerciseDays.get(playingExercise.currentExercise).getTotalKcal()));
        SharedPrefHelper.writeInteger(context, "kcal", 0);

        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CompleteFragment.this).commit();
                getActivity().finish();
            }
        });

        btnEditBmi.setOnClickListener(view12 -> showDialog());
        btnAddReminder.setOnClickListener(view12 -> startActivity(new Intent(context, ConfirmReminderActivity.class).putExtra("up", true)));
        btnMore.setOnClickListener(view12 -> startActivity(new Intent(context, CalenderActivity.class)));

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        setReminder(context);

        setDaysData(view);

        new getUserRecords().execute();

        return view;
    }

    public static void setReminder(Context context) {
        int hour = SharedPrefHelper.readInteger(context, context.getString(R.string.hour));
        int min = SharedPrefHelper.readInteger(context, context.getString(R.string.minute));
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hour, min);
        tvTime.setText(time);
    }

    private void setDaysData(View view) {
        RecyclerView rvHistory = view.findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(titles, date);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
    }

    private String getPlanName() {
        int i = playingExercise.currentPlan - 1;
        switch (i) {
            case 0:
                return "Beginner";
            case 1:
                return "Intermediate";
            case 2:
                return "Advanced";
            default:
                return "";
        }
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
    private class setUserRecord extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase.getInstance().recorddao().insertAll(record);
            AppDataBase.getInstance().userdao().updateUser(updateUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setRateAppDialog(context);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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
            initApp();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initApp() {
        float weight = user.getWeight();
        float height = user.getHeight();
        float bmi = ((weight) / (height * height)) * 703;
        tvBmi.setText(String.valueOf((int) bmi) + bmiCategory((int) bmi));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < recordList.size(); i++) {
            series.appendData(new DataPoint(recordList.get(i).getId() + 1, recordList.get(i).getWeight()), true, 30, false);
        }
        series.setColor(Color.BLUE);
        graph.addSeries(series);
        graph.setCursorMode(true);

        updateUser = user;
        updateUser.setTotalExcercise(user.getTotalExcercise() + Integer.parseInt(tvExerciseNo.getText().toString()));
        updateUser.setTotalKcal(user.getTotalKcal() + Integer.parseInt(tvKcal.getText().toString()));
        updateUser.setTotalTime(user.getTotalTime() + Integer.parseInt(tvTotalTime.getText().toString()));

        tvExerciseNo.setText(String.valueOf((playingExercise.totalExercisesPlayed + 1)));
        new setUserRecord().execute();

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

    public void setRateAppDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(context.getString(R.string.app_name));

        alertDialogBuilder
                .setMessage("Do you want to Rate us?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, id) -> {
                    dialog.cancel();
                    onRateUs(context);
                }).setNegativeButton("NO", (dialog, id) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void onRateUs(Context context) {
        SharedPrefHelper.writeBoolean(context, "rate", true);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        }
    }

}
