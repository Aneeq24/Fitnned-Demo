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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

public class NextFragment extends Fragment {

    PlayingExercise playingExercise;
    ImageView timerUp;
    ImageView skip;
    ImageView aimationImage;
    TextView nextSreenExerciseName;
    int pauseTimer = 0;
    public boolean pause = false;

    TextView textView;
    TextView exerciseTextView;

    CircleProgressBar mCustomCircleBar;

    ImageView pauseResumeImage;
    CountDownTimer countDownTimer;
    int currentRestTime = 0;
    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_next, container, false);

        LinearLayout linearLayout = rootView.findViewById(R.id.fbNative);
        pauseResumeImage = rootView.findViewById(R.id.nf_pauseTimerImageView);
        mCustomCircleBar = rootView.findViewById(R.id.restTimer);
        textView = rootView.findViewById(R.id.cf_roundText);
        exerciseTextView = rootView.findViewById(R.id.nf_exerciseText);
        aimationImage = rootView.findViewById(R.id.nf_exerciseImage);
        timerUp = rootView.findViewById(R.id.addRestTime); //addRestTime
        skip = rootView.findViewById(R.id.iv_timer);
        nextSreenExerciseName = rootView.findViewById(R.id.tv_nextHeadline);

        pause = false;

        context = getContext();

        AdsManager.getInstance().showFacebookNativeAd(Application.getContext(), linearLayout, null);
        playingExercise = (PlayingExercise) getActivity();

        mCustomCircleBar.setProgressFormatter((progress, max) -> progress + "s");

        assert playingExercise != null;
        mCustomCircleBar.setMax(playingExercise.restTime);

        if (playingExercise.currentRound <= (playingExercise.totalRounds - 1))
            textView.setText("Round " + (playingExercise.currentRound + 1) + "/" + playingExercise.totalRounds);
        else
            textView.setText("Round " + playingExercise.totalRounds + "/" + playingExercise.totalRounds);

        exerciseTextView.setText("Exercise " + (playingExercise.currentExercise + 1) + "/" + playingExercise.totalExercisePerRound);
        mCustomCircleBar.setProgress(playingExercise.restTime);
        mCustomCircleBar.setOnClickListener(view -> pauseOrRenume());
        startRestTimer(playingExercise.restTime * 1000);

        initNext();

        return rootView;
    }

    public void pauseOrRenume() {
        pause = !pause;

        if (pause) {
            pauseResumeImage.setImageResource(R.drawable.play_screen_play_icon);
            countDownTimer.cancel();
        } else {
            if (pauseTimer < 900) {
                pauseTimer *= 1000;
            }
            startRestTimer(pauseTimer);
            pauseResumeImage.setImageResource(R.drawable.play_screen_pause_btn);

        }

    }

    private void initNext() {

        skip.setOnClickListener(view13 -> playingExercise.StartPlayingFragment());
        timerUp.setOnClickListener(view1 -> addrestTime());

        nextSreenExerciseName.setText(playingExercise.nextExerciseName);

        String str = playingExercise.nextExerciseImage;
        int id = getResources().getIdentifier(str, "drawable", context.getPackageName());
        String path = "android.resource://" + context.getPackageName() + "/" + id;
        Glide.with(this).load(path).into(aimationImage);
        TTSManager.getInstance(playingExercise.getApplication()).play("Take a Rest for " + playingExercise.restTime + "seconds" + "The Next Exercise is " + playingExercise.nextExerciseName);
    }

    private void startRestTimer(int totalSkipTime) {

        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {

            public void onTick(long millisUntilFinished) {
                currentRestTime = (int) (millisUntilFinished / 1000);
                pauseTimer = currentRestTime;
                mCustomCircleBar.setProgress(currentRestTime);
                int id = getResources().getIdentifier("clock", "raw", context.getPackageName());
//                Utils.playAudio(rootView.getContext(),id);

                if (currentRestTime > 3)
                    Utils.playAudio(context, id);
                else
                    TTSManager.getInstance(playingExercise.getApplication()).play("" + currentRestTime);
            }

            public void onFinish() {
                int id = getResources().getIdentifier("ding", "raw", context.getPackageName());
                Utils.playAudio(context, id);

                mCustomCircleBar.setProgress(0);
                playingExercise.StartPlayingFragment();
            }
        }.start();

    }

    private void addrestTime() {

        countDownTimer.cancel();
        if (!pause) {
            currentRestTime *= 1000;
            currentRestTime += 5000;
            mCustomCircleBar.setMax(currentRestTime / 1000);
            mCustomCircleBar.setProgress(currentRestTime / 1000);
            startRestTimer(currentRestTime);
        } else {
            pauseTimer += 5;
            mCustomCircleBar.setMax(pauseTimer);
            mCustomCircleBar.setProgress(pauseTimer);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }


}
