package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.google.android.gms.ads.AdView;


public class PauseFragment extends Fragment {

    View rootView;
    TextView tvExName;
    TextView tvExercise;
    ImageView btnResume;
    ImageView imgAnimate;
    PlayingExercise mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pause, container, false);

        AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        findReferences();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void findReferences() {
        mActivity = (PlayingExercise) getActivity();
        btnResume = rootView.findViewById(R.id.pauseResume);
        tvExName = rootView.findViewById(R.id.tv_pauseHeadline);
        imgAnimate = rootView.findViewById(R.id.pf_exerciseImage);
        tvExercise = rootView.findViewById(R.id.pf_exerciseText);

        if ((mActivity.currentEx + 1) != mActivity.totalExercises) {
            tvExName.setText(mActivity.nextExerciseName);

            int id = mActivity.getResources().getIdentifier(mActivity.nextExerciseImage, "drawable", mActivity.getPackageName());
            if (id != 0) {
                String path = "android.resource://" + mActivity.getPackageName() + "/" + id;
                Glide.with(this).load(mActivity.nextExerciseUrl).thumbnail(Glide.with(this).load(path)).into(imgAnimate);
            }
        }

        btnResume.setOnClickListener(view -> onResumeExercise());

        if (mActivity.currentEx <= (mActivity.totalExercises - 1))
            tvExercise.setText("Exercise " + (mActivity.currentEx + 1) + " of " + mActivity.totalExercises);
        else
            tvExercise.setText("Exercise " + mActivity.totalExercises + " of " + mActivity.totalExercises);
    }

    private void onResumeExercise() {
        mActivity.onResumeFragment();
    }

}
