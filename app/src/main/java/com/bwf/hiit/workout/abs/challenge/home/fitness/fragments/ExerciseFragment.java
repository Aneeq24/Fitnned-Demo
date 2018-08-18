package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.Objects;


public class ExerciseFragment extends Fragment {

    int value = 0;
    int soundValue;
    int reamingTime;
    Context context;
    CountDownTimer countDownTimer;
    PlayingExercise mActivity;
    CircleProgressBar progressTimer;
    TextView tvExName;
    TextView tvRound;
    TextView tvExercise;
    ImageView btnPause;
    ImageView imgExercise;
    ImageView btnHelp;
    ImageView btnSound;
    ImageView btnPrevious;
    ImageView btnNext;
    FloatingActionButton fabNoAds;
    FloatingActionButton fabRateUs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        mActivity = (PlayingExercise) getActivity();
        context = getContext();

        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        imgExercise = rootView.findViewById(R.id.animationImage);
        tvExercise = rootView.findViewById(R.id.ef_exerciseTextView);
        tvRound = rootView.findViewById(R.id.ef_roundTextView);
        btnHelp = rootView.findViewById(R.id.btn_help);
        btnPause = rootView.findViewById(R.id.btn_pause);
        btnNext = rootView.findViewById(R.id.btn_next);
        btnPrevious = rootView.findViewById(R.id.btn_previous);
        tvExName = rootView.findViewById(R.id.tv_exerciseName_Playing);
        btnSound = rootView.findViewById(R.id.btn_sound);
        progressTimer = rootView.findViewById(R.id.prog_timer);
        fabNoAds = rootView.findViewById(R.id.fab_no_ads);
        fabRateUs = rootView.findViewById(R.id.fab_rate_us);

        progressTimer.setProgressFormatter((progress, max) -> progress + "\"");
        progressTimer.setOnClickListener(view -> pause());
        btnSound.setOnClickListener(view -> soundButton());
        btnPause.setOnClickListener(view -> pause());
        btnHelp.setOnClickListener(view -> helpButtonClick());
        fabNoAds.setOnClickListener(view -> mActivity.mBilling.purchaseRemoveAds());
        fabRateUs.setOnClickListener(view -> onRateUs());
        btnPrevious.setOnClickListener(view -> {

        });
        btnNext.setOnClickListener(view -> {
            mActivity.NextFragment();
        });

        findRefrence();
        return rootView;
    }

    private void findRefrence() {
        if (soundValue > 0)
            btnSound.setImageResource(R.drawable.play_screen_sound_on_btn);
        else
            btnSound.setImageResource(R.drawable.play_screen_sound_off_btn);

        soundValue = SharedPrefHelper.readInteger(context, "sound");

        assignTopUi();
        String str = mActivity.exerciseName;
        tvExName.setText(mActivity.displayName);

        int id = getResources().getIdentifier(str, "drawable", context.getPackageName());
        String path = "android.resource://" + context.getPackageName() + "/" + id;
        Glide.with(context).load(path).into(imgExercise);

        if (!PlayingExercise.is_Paused) {
            value = mActivity.getCurrentReps();
            TTSManager.getInstance(mActivity.getApplication()).play("Do " + mActivity.displayName + " for " + value / 1000 + " seconds");
            startPlayingExercise(value);
        } else {
            PlayingExercise.is_Paused = false;
            int val = PlayingExercise.pauseTimer;
            PlayingExercise.pauseTimer = 0;
            val = (val) * 1000;
            startPlayingExercise(val);
        }
    }

    private void soundButton() {
        if (soundValue > 0) {
            soundValue = 0;
            btnSound.setImageResource(R.drawable.play_screen_sound_off_btn);
        } else {
            soundValue = 1;
            btnSound.setImageResource(R.drawable.play_screen_sound_on_btn);
        }
        SharedPrefHelper.writeInteger(context, "sound", soundValue);
    }

    @SuppressLint("SetTextI18n")
    private void assignTopUi() {
        tvRound.setText((mActivity.currentRound + 1) + " of " + mActivity.totalRounds);
        tvExercise.setText((mActivity.currentExercise + 1) + " of " + mActivity.totalExercisePerRound);
    }

    private void startPlayingExercise(int totalSkipTime) {
        progressTimer.setMax(value / 1000);
        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                reamingTime = (int) (millisUntilFinished / 1000);
                int id = getResources().getIdentifier("clock", "raw", Objects.requireNonNull(getContext()).getPackageName());

                if (reamingTime > 3) {
                    Utils.playAudio(getContext(), id);
                } else {
                    TTSManager.getInstance(mActivity.getApplication()).play("" + reamingTime);
                }

                progressTimer.setProgress(reamingTime);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                int id = getResources().getIdentifier("ding", "raw", Objects.requireNonNull(getContext()).getPackageName());
                Utils.playAudio(getContext(), id);
                onExerciseComplete();
            }
        }.start();

    }

    private void onExerciseComplete() {
        float prevKcal = SharedPrefHelper.readInteger(context, "kcal");
        int currentKcal = (int) (mActivity.exerciseKcal + prevKcal);
        SharedPrefHelper.writeInteger(context, "kcal", currentKcal);
        mActivity.NextFragment();
    }

    public void pause() {
        countDownTimer.cancel();
        mActivity.PauseFragment(reamingTime);
        reamingTime = 0;
    }

    private void helpButtonClick() {
        countDownTimer.cancel();
        mActivity.helpFragmentFun(reamingTime);
        reamingTime = 0;
    }

    public void onRateUs() {
        AnalyticsManager.getInstance().sendAnalytics("rate_us_clicked_done", "Rate_us");
        SharedPrefHelper.writeBoolean(context, "rate", true);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }


}
