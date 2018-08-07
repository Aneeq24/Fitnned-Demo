package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;


public class PauseFragment extends Fragment {

    TextView nextExerciseName;
    TextView tvRound;
    TextView tvExercise;
    View rootView;
    ImageView btnResume;
    ImageView imgAnimate;
    PlayingExercise playingExercise;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pause, container, false);

        LinearLayout fbNative = rootView.findViewById(R.id.fbNative);

        AdsManager.getInstance().showFacebookNativeAd(Application.getContext(),fbNative ,null);
        findReferences();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void  findReferences() {

        playingExercise = (PlayingExercise) getActivity();
        btnResume = rootView.findViewById(R.id.pauseResume);
        nextExerciseName = rootView.findViewById(R.id.tv_pauseHeadline);
        imgAnimate = rootView.findViewById(R.id.pf_exerciseImage);
        tvRound = rootView.findViewById(R.id.pf_roundText);
        tvExercise = rootView.findViewById(R.id.pf_exerciseText);

        nextExerciseName.setText(playingExercise.nextExerciseName);

        String str =  playingExercise.nextExerciseImage;
        int id = getResources().getIdentifier(str, "drawable",rootView.getContext().getPackageName());
        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

        Glide.with(this).load(path).into(imgAnimate);

        btnResume.setOnClickListener(view -> onResumeExercise());

        if (playingExercise.currentRound<=(playingExercise.totalRounds-1))
            tvRound.setText("Round " + (playingExercise.currentRound +1)+"/" + playingExercise.totalRounds);
        else
            tvRound.setText("Round " + playingExercise.totalRounds+"/" + playingExercise.totalRounds);

        tvExercise.setText("Exercise " + playingExercise.currentExercise + "/" + playingExercise.totalExercisePerRound);
      }

    private void  onResumeExercise() {
        playingExercise.onResumeFragment();
    }


}
