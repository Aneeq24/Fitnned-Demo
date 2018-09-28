package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.WorkoutAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.HomeActivity;

public class WorkoutFragment extends Fragment {

    int plan = 0;
    Context context;
    RecyclerView rvScroll;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragement_workout, container, false);

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "day_selection_activity");
        rvScroll = rootView.findViewById(R.id.rv_scroll);

        HomeActivity.tvTitle.setText("Exercise");
        context = getContext();
        Bundle intent = getArguments();
        if (intent != null && intent.containsKey(getString(R.string.plan)))
            plan = intent.getInt(getString(R.string.plan), 0);

        initView();

        return rootView;
    }

    private void initView() {
        rvScroll.setLayoutManager(new LinearLayoutManager(context));
        WorkoutAdapter mAdapter = new WorkoutAdapter(context, plan);
        rvScroll.setAdapter(mAdapter);
    }

}
