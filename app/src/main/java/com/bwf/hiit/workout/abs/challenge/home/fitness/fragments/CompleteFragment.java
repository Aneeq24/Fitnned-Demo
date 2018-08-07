package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

public class CompleteFragment extends Fragment {

    Toolbar toolbar;
    TextView tvExerciseNo;
    TextView tvTotalTime;
    TextView tvKcal;
    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        toolbar = view.findViewById(R.id.toolbar10);
        tvExerciseNo = view.findViewById(R.id.tv_exerciseNo);
        tvTotalTime = view.findViewById(R.id.tv_totalTime);
        tvKcal = view.findViewById(R.id.tv_kcal);

        context = getContext();

        PlayingExercise playingExercise = (PlayingExercise) getActivity();

        assert playingExercise != null;
        int minutes = (playingExercise.totaTimeSpend % 3600) / 60;
        int seconds = playingExercise.totaTimeSpend % 60;

        @SuppressLint("DefaultLocale") String timeString = String.format("%02d:%02d", minutes, seconds);

        tvExerciseNo.setText("" + playingExercise.totalExercises);
        tvTotalTime.setText("" + timeString);
        tvKcal.setText(SharedPrefHelper.readInteger(context, "kcal"));

        toolbar.setNavigationOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CompleteFragment.this).commit();
                getActivity().finish();
            }
        });

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        return view;
    }
}
