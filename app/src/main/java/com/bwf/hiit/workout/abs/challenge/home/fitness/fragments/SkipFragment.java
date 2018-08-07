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

import java.util.Objects;

public class SkipFragment extends Fragment {


    CircleProgressBar skipCircleprogressBar;
    View rootView;
    TextView headingNameExercise;
    PlayingExercise playingExercise;
    TextView skipTimerText;
    TextView skipTimerButton;
    ImageView skipImg;
    CountDownTimer countDownTimer;
    VideoView skipExerciseVideoView;
    CountDownTimer videoStartTimer;
    ImageView soundButton_B;

    int pauseTimer = 0;
    public boolean pause = false;
    ImageView pauseResumeImage;
    int soundValue;
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_skip, container, false);
        context = getContext();
        pauseResumeImage = rootView.findViewById(R.id.sf_pauseTimerImageView);
        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);
        skipCircleprogressBar = rootView.findViewById(R.id.skipExerciseTimeCircle);
        findReferences();
        return  rootView;

    }

   public   void  pauseOrRenume()
    {
        pause = !pause;

        if (pause)
        {
            pauseResumeImage.setImageResource(R.drawable.play_screen_play_icon);
            countDownTimer.cancel();
        }
        else
        {
            pauseTimer+=1;
            pauseTimer *= 1000;
            startSkipTimer(pauseTimer , skipTimerText);
            pauseResumeImage.setImageResource(R.drawable.play_screen_pause_btn);

        }

    }


    void  soundButton()
    {

        if (soundValue>0)
        {
            soundValue = 0;
            soundButton_B.setImageResource(R.drawable.play_screen_sound_off_btn);
        }
        else
        {
            soundValue = 1;
            soundButton_B.setImageResource(R.drawable.play_screen_sound_on_btn);
        }

        SharedPrefHelper.writeInteger(context,"sound",soundValue);

    }

    void  findReferences()
    {
        playingExercise = (PlayingExercise)getActivity();
        skipTimerText = rootView.findViewById(R.id.timerSkipText);
        skipTimerButton = rootView.findViewById(R.id.skipButton);
        skipImg = rootView.findViewById(R.id.skipLayoutImag);
        skipExerciseVideoView = rootView.findViewById(R.id.sf_exerciseVideoView);
        headingNameExercise = rootView.findViewById(R.id.tv_exercise_name_skipScreen);

        headingNameExercise.setText(playingExercise.displayName);

        skipTimerButton.setOnClickListener(view -> startPlayingButton());


        startSkipTimer(11000 , skipTimerText);

        skipCircleprogressBar.setProgressFormatter((progress, max) -> progress + "\"");

        skipCircleprogressBar.setMax(15);

        skipCircleprogressBar.setOnClickListener(view -> pauseOrRenume());


        soundButton_B = rootView.findViewById(R.id.sf_soundFragment);

        soundValue = SharedPrefHelper.readInteger(context,"sound");

        if(soundValue>0)
        {
            soundButton_B.setImageResource(R.drawable.play_screen_sound_on_btn);
        }
        else
        {
            soundButton_B.setImageResource(R.drawable.play_screen_sound_off_btn);
        }

        soundButton_B.setOnClickListener(view -> soundButton());



        String str =  playingExercise.exerciseName;
        int id = getResources().getIdentifier(str, "drawable",rootView.getContext().getPackageName());

        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

        Glide.with(this).load(path).into(skipImg);

    }

    void  startVideo(int totalskipTime , int interval)

    {
        videoStartTimer = new CountDownTimer(totalskipTime , interval)
        {
            @Override
            public void onTick(long l)
            {

            }

            @Override
            public void onFinish()
            {
                skipExerciseVideoView.setAlpha(1);
            }
        }.start();
    }

    void startSkipTimer(int totalSkipTime, final TextView timer) {

        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished)
            {
                timer.setText("" + millisUntilFinished / 1000 + "\"");
                int value = (int)(millisUntilFinished / 1000);
                pauseTimer = value;
                skipCircleprogressBar.setProgress(value);
                int id = getResources().getIdentifier("clock", "raw",rootView.getContext().getPackageName());
                if (value>3)
                Utils.playAudio(rootView.getContext(),id);
                else
                    TTSManager.getInstance(playingExercise.getApplication()).play("" +value);
            }

            public void onFinish()
            {
                //skipCircleprogressBar.setProgress(0);

                int id = getResources().getIdentifier("ding", "raw",rootView.getContext().getPackageName());
                Utils.playAudio(rootView.getContext() , id);

                startPlayingButton();
            }
        }.start();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       countDownTimer.cancel();
      //  skipExerciseVideoView.setAlpha(0);
      //  videoStartTimer.cancel();
    }

    void  startPlayingButton()
    {
        countDownTimer.cancel();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(SkipFragment.this).commit();
        playingExercise.StartPlayingFragment();
    }

}
