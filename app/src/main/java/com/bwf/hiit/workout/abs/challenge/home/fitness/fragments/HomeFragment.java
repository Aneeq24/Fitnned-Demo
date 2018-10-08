package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.HomeAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.CalenderActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    List<Integer> progress;
    Context context;

    TextView tvKcal;
    TextView tvTotalMin;
    TextView tvTotalTime;
    HomeAdapter mAdapter;
    TextView tvExerciseNo;
    RelativeLayout btnMore;
    RecyclerView rvHomeScreen;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        HomeActivity.tvTitle.setText("Plans");

        tvExerciseNo = rootView.findViewById(R.id.tv_exercise);
        tvTotalMin = rootView.findViewById(R.id.tv_time);
        tvTotalTime = rootView.findViewById(R.id.tv_mins);
        tvKcal = rootView.findViewById(R.id.tv_kcal);
        btnMore = rootView.findViewById(R.id.btn_more);
        rvHomeScreen = rootView.findViewById(R.id.rv_mainMenu);

        try {
            new getData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initApp();
        setDaysData(rootView);

        btnMore.setOnClickListener(view12 -> startActivity(new Intent(context, CalenderActivity.class)));

        return rootView;
    }

    private void setDaysData(View view) {
        RecyclerView rvHistory = view.findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(days);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
    }

    private void initApp() {
        rvHomeScreen.setNestedScrollingEnabled(false);
        rvHomeScreen.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new HomeAdapter(context);
        rvHomeScreen.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            new getData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase dataBase = AppDataBase.getInstance();
            progress = new ArrayList<>();
            for (int plan = 1; plan < 4; plan++) {
                int val = 0;
                for (int i = 1; i <= 30; i++) {
                    if (dataBase.exerciseDayDao().getExerciseDays(plan, i).size() > 0) {
                        int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan, i).get(0).getExerciseComplete();
                        int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan, i).get(0).getTotalExercise();
                        float v = (float) totalComplete / (float) totalExercises;
                        if (v >= 1) {
                            val++;
                        }
                    }
                }
                progress.add(val);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.setProgress(progress);
        }
    }

}


