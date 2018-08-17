package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.Objects;


public class ExerciseFragment extends Fragment {

    int value = 0;
    int soundValue;
    int remaingTime;

    Context context;
    CountDownTimer countDownTimer;
    PlayingExercise playingExercise;

    VideoView viewVideo;
    CircleProgressBar playingExerciseCircle;
    TextView exerciseName;

    TextView roundTextView;
    TextView exerciseTextView;
    TextView exerciseTimer;
    Button pauseButton;
    ImageView exerciseImage;
    ImageView helpButton;

    ImageView soundButton_B;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        playingExercise = (PlayingExercise) getActivity();
        context = getContext();

        exerciseTimer = rootView.findViewById(R.id.timerExerciseText);
        pauseButton = rootView.findViewById(R.id.pause);
        exerciseImage = rootView.findViewById(R.id.animationImage);
        viewVideo = rootView.findViewById(R.id.videoViewId);
        exerciseTextView = rootView.findViewById(R.id.ef_exerciseTextView);
        roundTextView = rootView.findViewById(R.id.ef_roundTextView);
        helpButton = rootView.findViewById(R.id.help);
        exerciseName = rootView.findViewById(R.id.tv_exerciseName_Playing);
        soundButton_B = rootView.findViewById(R.id.ef_soundFragment);
        playingExerciseCircle = rootView.findViewById(R.id.playingExericseCircle);
        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        playingExerciseCircle.setProgressFormatter((progress, max) -> progress + "\"");
        playingExerciseCircle.setOnClickListener(view -> pause());

        soundButton_B.setOnClickListener(view -> soundButton());

        pauseButton.setOnClickListener(view -> pause());

        helpButton.setOnClickListener(view -> helpButtonClick());
        findRefrence();

        return rootView;
    }

    private void soundButton() {
        if (soundValue > 0) {
            soundValue = 0;
            soundButton_B.setImageResource(R.drawable.play_screen_sound_off_btn);
        } else {
            soundValue = 1;
            soundButton_B.setImageResource(R.drawable.play_screen_sound_on_btn);
        }

        SharedPrefHelper.writeInteger(context, "sound", soundValue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    private void findRefrence() {

        if (soundValue > 0)
            soundButton_B.setImageResource(R.drawable.play_screen_sound_on_btn);
        else
            soundButton_B.setImageResource(R.drawable.play_screen_sound_off_btn);

        soundValue = SharedPrefHelper.readInteger(context, "sound");

        assignTopUi();
        String str = playingExercise.exerciseName;
        exerciseName.setText(playingExercise.displayName);

        int id = getResources().getIdentifier(str, "drawable", context.getPackageName());
        String path = "android.resource://" + context.getPackageName() + "/" + id;
        Glide.with(context).load(path).into(exerciseImage);

        if (!PlayingExercise.is_Paused) {
            value = playingExercise.getCurrentReps();
            TTSManager.getInstance(playingExercise.getApplication()).play("Do " + playingExercise.displayName + " for " + value / 1000 + " seconds");
            startPlayingExercise(value);
        } else {
            PlayingExercise.is_Paused = false;
            int val = PlayingExercise.pauseTimer;
            PlayingExercise.pauseTimer = 0;
            val = (val) * 1000;
            startPlayingExercise(val);
        }
    }

    @SuppressLint("SetTextI18n")
    private void assignTopUi() {
        roundTextView.setText((playingExercise.currentRound + 1) + " of " + playingExercise.totalRounds);
        exerciseTextView.setText((playingExercise.currentExercise + 1) + " of " + playingExercise.totalExercisePerRound);
    }

    private void startPlayingExercise(int totalSkipTime) {

        playingExerciseCircle.setMax(value / 1000);
        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                remaingTime = (int) (millisUntilFinished / 1000);
                int id = getResources().getIdentifier("clock", "raw", Objects.requireNonNull(getContext()).getPackageName());

                if (remaingTime > 3) {
                    Utils.playAudio(getContext(), id);
                } else {
                    TTSManager.getInstance(playingExercise.getApplication()).play("" + remaingTime);
                }

                playingExerciseCircle.setProgress(remaingTime);
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
        int currentKcal = (int) (playingExercise.exerciseKcal + prevKcal);
        SharedPrefHelper.writeInteger(context, "kcal", currentKcal);
        playingExercise.NextFragment();
    }

    public void pause() {
        countDownTimer.cancel();
        playingExercise.PauseFragment(remaingTime);
        remaingTime = 0;
    }

    private void helpButtonClick() {
        countDownTimer.cancel();
        playingExercise.helpFragmentFun(remaingTime);
        remaingTime = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

}
