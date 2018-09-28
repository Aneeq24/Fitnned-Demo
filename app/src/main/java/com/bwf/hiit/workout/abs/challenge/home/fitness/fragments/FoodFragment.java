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
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.FoodAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.HomeActivity;

public class FoodFragment extends Fragment {

    Context context;
    RecyclerView rvScroll;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragement_food, container, false);

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "day_selection_activity");
        rvScroll = rootView.findViewById(R.id.rv_scroll);

        HomeActivity.tvTitle.setText("Food");
        context = getContext();

        initView();

        return rootView;
    }

    private void initView() {
        rvScroll.setLayoutManager(new LinearLayoutManager(context));
        FoodAdapter mAdapter = new FoodAdapter(context);
        rvScroll.setAdapter(mAdapter);
    }
}
