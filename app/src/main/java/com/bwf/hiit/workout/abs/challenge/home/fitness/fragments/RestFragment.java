package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RestFragment extends Fragment {

    PlayingExercise playingExercise;
    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rest, container, false);

        playingExercise = (PlayingExercise) getActivity();

        AdsManager.getInstance().showFacebookInterstitialAd();

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        playingExercise.onBackPressed();
    }
}
