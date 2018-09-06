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

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

public class SkipFragment extends Fragment {

    View rootView;
    PlayingExercise playingExercise;
    CircleProgressBar progressBar;
    CountDownTimer countDownTimer;
    TextView tvExName;
    TextView btnSkip;
    ImageView imgAnim;
    ImageView btnSound;
    ImageView btnPause;
    int pauseTimer = 0;
    int soundValue;
    Context context;
    public boolean pause = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_skip, container, false);
        context = getContext();
        btnPause = rootView.findViewById(R.id.btn_pause_play);
        progressBar = rootView.findViewById(R.id.skipExerciseTimeCircle);

        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        findReferences();

        return rootView;
    }

    public void pauseOrRenume() {
        pause = !pause;
        if (pause) {
            btnPause.setImageResource(R.drawable.play_screen_play_icon);
            countDownTimer.cancel();
        } else {
            pauseTimer += 1;
            pauseTimer *= 1000;
            startSkipTimer(pauseTimer);
            btnPause.setImageResource(R.drawable.play_screen_pause_btn);
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

    private void findReferences() {
        playingExercise = (PlayingExercise) getActivity();
        btnSkip = rootView.findViewById(R.id.skipButton);
        imgAnim = rootView.findViewById(R.id.skipLayoutImag);
        tvExName = rootView.findViewById(R.id.tv_exercise_name_skipScreen);
        tvExName.setText(playingExercise.displayName);
        btnSound = rootView.findViewById(R.id.sf_soundFragment);

        startSkipTimer(11000);

        progressBar.setProgressFormatter((progress, max) -> progress + "\"");
        progressBar.setMax(15);
        soundValue = SharedPrefHelper.readInteger(context, "sound");

        if (soundValue > 0)
            btnSound.setImageResource(R.drawable.play_screen_sound_on_btn);
         else
            btnSound.setImageResource(R.drawable.play_screen_sound_off_btn);

        btnSkip.setOnClickListener(view -> startPlayingButton());
        progressBar.setOnClickListener(view -> pauseOrRenume());
        btnSound.setOnClickListener(view -> soundButton());

        String str = playingExercise.exerciseName;
        int id = context.getResources().getIdentifier(str, "drawable", rootView.getContext().getPackageName());
        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;
        Glide.with(this).load(path).into(imgAnim);

    }

    private void startSkipTimer(int totalSkipTime) {

        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                int value = (int) (millisUntilFinished / 1000);
                pauseTimer = value;
                progressBar.setProgress(value);
                int id = getResources().getIdentifier("clock", "raw", rootView.getContext().getPackageName());
                if (value > 3)
                    Utils.playAudio(rootView.getContext(), id);
                else
                    TTSManager.getInstance(playingExercise.getApplication()).play("" + value);
            }

            public void onFinish() {
                int id = getResources().getIdentifier("ding", "raw", rootView.getContext().getPackageName());
                Utils.playAudio(rootView.getContext(), id);
                startPlayingButton();
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    private void startPlayingButton() {
        countDownTimer.cancel();
        playingExercise.getSupportFragmentManager().beginTransaction().remove(SkipFragment.this).commitAllowingStateLoss();
        playingExercise.StartPlayingFragment();
    }

}
