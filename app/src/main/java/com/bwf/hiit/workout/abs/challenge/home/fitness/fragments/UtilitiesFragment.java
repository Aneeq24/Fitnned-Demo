package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.BmiActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.BodyFatActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.HomeActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ProteinActivity;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.WeightLossActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UtilitiesFragment extends Fragment {

    Context context;
    Unbinder unbinder;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_utilities, container, false);
        context = getContext();
        HomeActivity.tvTitle.setText("Tools");
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_bmi, R.id.btn_protein, R.id.btn_fat, R.id.btn_calories})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bmi:
                startActivity(new Intent(context, BmiActivity.class));
                break;
            case R.id.btn_protein:
                startActivity(new Intent(context, ProteinActivity.class));
                break;
            case R.id.btn_fat:
                startActivity(new Intent(context, BodyFatActivity.class));
                break;
            case R.id.btn_calories:
                startActivity(new Intent(context, WeightLossActivity.class));
                break;
        }
    }

}
