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
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdView;

public class NextFragment extends Fragment {

    PlayingExercise mActivity;
    ImageView btnSkip;
    ImageView imgAnim;
    ImageView btnResume;
    ImageView btnTimerUp;
    TextView txtExercise;
    TextView txtExerciseName;
    int pauseTimer = 0;
    public boolean pause = false;
    CircleProgressBar mCustomCircleBar;
    CountDownTimer countDownTimer;
    int currentRestTime = 0;
    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_next, container, false);

        AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        btnResume = rootView.findViewById(R.id.nf_pauseTimerImageView);
        mCustomCircleBar = rootView.findViewById(R.id.restTimer);
        txtExercise = rootView.findViewById(R.id.nf_exerciseText);
        imgAnim = rootView.findViewById(R.id.nf_exerciseImage);
        btnTimerUp = rootView.findViewById(R.id.addRestTime); //addRestTime
        btnSkip = rootView.findViewById(R.id.iv_timer);
        txtExerciseName = rootView.findViewById(R.id.tv_nextHeadline);

        pause = false;
        context = getContext();
        mActivity = (PlayingExercise) getActivity();
        mCustomCircleBar.setProgressFormatter((progress, max) -> progress + "s");

        assert mActivity != null;
        mCustomCircleBar.setMax(mActivity.restTime);

        txtExercise.setText("Exercise " + (mActivity.currentEx + 1) + " of " + mActivity.totalExercisePerRound);
        mCustomCircleBar.setProgress(mActivity.restTime);
        mCustomCircleBar.setOnClickListener(view -> pauseOrRenume());
        startRestTimer(mActivity.restTime * 1000);

        if (mActivity.iscomplete)
            mActivity.StartPlayingFragment();

        initNext();

        return rootView;
    }

    public void pauseOrRenume() {
        pause = !pause;

        if (pause) {
            btnResume.setImageResource(R.drawable.play_screen_play_icon);
            countDownTimer.cancel();
        } else {
            if (pauseTimer < 900) {
                pauseTimer *= 1000;
            }
            startRestTimer(pauseTimer);
            btnResume.setImageResource(R.drawable.play_screen_pause_btn);
        }
    }

    private void initNext() {

        btnSkip.setOnClickListener(view13 -> mActivity.StartPlayingFragment());
        btnTimerUp.setOnClickListener(view1 -> addrestTime());

        txtExerciseName.setText(mActivity.displayName);

        String str = mActivity.exerciseName;
        int id = getResources().getIdentifier(str, "drawable", context.getPackageName());
        String path = "android.resource://" + context.getPackageName() + "/" + id;
        Glide.with(this).load(path).into(imgAnim);
        TTSManager.getInstance(mActivity.getApplication()).play("Take a Rest for " + mActivity.restTime + "seconds" + "The Next Exercise is " + mActivity.displayName);
    }

    private void startRestTimer(int totalSkipTime) {

        countDownTimer = new CountDownTimer(totalSkipTime, 1000) {

            public void onTick(long millisUntilFinished) {
                currentRestTime = (int) (millisUntilFinished / 1000);
                pauseTimer = currentRestTime;
                mCustomCircleBar.setProgress(currentRestTime);
                int id = getResources().getIdentifier("clock", "raw", context.getPackageName());

                if (currentRestTime == (totalSkipTime / 1000) / 2)
                    TTSManager.getInstance(mActivity.getApplication()).play("You have " + currentRestTime + " sec remaining");

                if (currentRestTime > 3)
                    Utils.playAudio(context, id);
                else
                    TTSManager.getInstance(mActivity.getApplication()).play("" + currentRestTime);
            }

            public void onFinish() {
                int id = getResources().getIdentifier("ding", "raw", context.getPackageName());
                Utils.playAudio(context, id);

                mCustomCircleBar.setProgress(0);

                mActivity.StartPlayingFragment();
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
