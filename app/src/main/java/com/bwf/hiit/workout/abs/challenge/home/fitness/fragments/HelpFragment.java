package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

public class HelpFragment extends Fragment {

    PlayingExercise playingExercise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_help, container, false);

        playingExercise = (PlayingExercise) getActivity();
        ImageView closeButton  =  rootView.findViewById(R.id.closeButton);

        AdsManager.getInstance().showFacebookInterstitialAd();

        closeButton.setOnClickListener(view -> closeButtonClick());
        return rootView;
    }

    private void  closeButtonClick() {
        playingExercise.onResumeFragment();
    }

}
