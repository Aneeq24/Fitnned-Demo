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
import com.google.android.gms.ads.AdView;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExerciseFragment extends Fragment {

    int value = 0;
    int soundValue;
    int remaingTime;

    Context context;
    CountDownTimer countDownTimer;
    PlayingExercise playingExercise;

    TextView tvRound;
    TextView tvExercise;
    TextView tvExerciseName;
    ImageView btnPause;
    ImageView btnSound;
    ImageView animationImage;
    AdView banerAdmob;
    VideoView videoViewId;
    CircleProgressBar playingExericseCircle;
    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        playingExercise = (PlayingExercise) getActivity();
        context = getContext();

        playingExericseCircle = rootView.findViewById(R.id.playingExericseCircle);
        btnPause = rootView.findViewById(R.id.btn_pause);
        btnSound = rootView.findViewById(R.id.btn_sound);
        tvExercise = rootView.findViewById(R.id.tv_exercise);
        tvRound = rootView.findViewById(R.id.tv_round);
        tvExerciseName = rootView.findViewById(R.id.tv_exercise_name);
        animationImage = rootView.findViewById(R.id.animationImage);
        videoViewId = rootView.findViewById(R.id.videoViewId);
        banerAdmob = rootView.findViewById(R.id.baner_Admob);

        playingExericseCircle.setProgressFormatter((progress, max) -> progress + "\"");
        playingExericseCircle.setOnClickListener(view -> pause());

        findRefrence();
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
        unbinder.unbind();
    }

    private void findRefrence() {

        AdsManager.getInstance().showBanner(banerAdmob);
        soundValue = SharedPrefHelper.readInteger(context, "sound");

        if (soundValue > 0)
            btnSound.setImageResource(R.drawable.play_screen_sound_on_btn);
        else
            btnSound.setImageResource(R.drawable.play_screen_sound_off_btn);

        assignTopUi();
        String str = playingExercise.exerciseName;
        tvExerciseName.setText(playingExercise.displayName);
        int id = getResources().getIdentifier(str, "drawable", context.getPackageName());
        String path = "android.resource://" + context.getPackageName() + "/" + id;
        Glide.with(context).load(path).into(animationImage);

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
        tvRound.setText((playingExercise.currentRound + 1) + "/" + playingExercise.totalRounds);
        tvExercise.setText((playingExercise.currentExercise + 1) + "/" + playingExercise.totalExercisePerRound);
    }

    private void startPlayingExercise(int totalSkipTime) {

        playingExericseCircle.setMax(value / 1000);
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

                playingExericseCircle.setProgress(remaingTime);
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

    @OnClick({R.id.btn_sound, R.id.btn_video, R.id.btn_help, R.id.btn_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sound:
                soundButton();
                break;
            case R.id.btn_video:

                break;
            case R.id.btn_help:
                helpButtonClick();
                break;
            case R.id.btn_pause:
                pause();
                break;
        }
    }
}
