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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.ads.AdView;

public class NextFragment extends Fragment {

    Context context;
    ImageView btnSkip;
    ImageView imgAnim;
    int pauseTimer = 0;
    ImageView btnResume;
    ImageView btnTimerUp;
    TextView txtExercise;
    CountDownTimer mTimer;
    int currentRestTime = 0;
    TextView txtExerciseName;
    PlayingExercise mActivity;
    public boolean pause = false;
    CircleProgressBar mCircleBar;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_next, container, false);

        AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        btnResume = rootView.findViewById(R.id.nf_pauseTimerImageView);
        mCircleBar = rootView.findViewById(R.id.restTimer);
        txtExercise = rootView.findViewById(R.id.nf_exerciseText);
        imgAnim = rootView.findViewById(R.id.nf_exerciseImage);
        btnTimerUp = rootView.findViewById(R.id.addRestTime); //addRestTime
        btnSkip = rootView.findViewById(R.id.iv_timer);
        txtExerciseName = rootView.findViewById(R.id.tv_nextHeadline);

        pause = false;
        context = getContext();
        mActivity = (PlayingExercise) getActivity();
        mCircleBar.setProgressFormatter((progress, max) -> progress + "s");

        assert mActivity != null;
        mCircleBar.setMax(mActivity.restTime);

        txtExercise.setText("Exercise " + (mActivity.currentEx + 1) + " of " + mActivity.totalExercises);
        mCircleBar.setProgress(mActivity.restTime);
        mCircleBar.setOnClickListener(view -> pauseOrResume());
        startRestTimer(mActivity.restTime * 1000);

        if (mActivity.isComplete)
            mActivity.StartPlayingFragment();

        initNext();

        return rootView;
    }

    public void pauseOrResume() {
        pause = !pause;

        if (pause) {
            btnResume.setImageResource(R.drawable.play_screen_play_icon);
            mTimer.cancel();
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
        btnTimerUp.setOnClickListener(view1 -> addressTime());

        txtExerciseName.setText(mActivity.displayName);

        int id = getResources().getIdentifier(mActivity.exerciseImage, "drawable", mActivity.getPackageName());
        if (id != 0) {
            String path = "android.resource://" + mActivity.getPackageName() + "/" + id;
            Glide.with(this).load(path).into(imgAnim);
        } else if (SharedPrefHelper.readBoolean(mActivity, getString(R.string.is_load))) {
            String temp = mActivity.getCacheDir().getAbsolutePath() + "/" + mActivity.exerciseImage + ".gif";
            Glide.with(this).load(temp).into(imgAnim);
        } else {
            Glide.with(this).load(mActivity.exerciseUrl).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(imgAnim);
        }

        TTSManager.getInstance(mActivity.getApplication()).play("Take a Rest for " + mActivity.restTime + "seconds" + "The Next Exercise is " + mActivity.displayName);
    }

    private void startRestTimer(int totalSkipTime) {

        mTimer = new CountDownTimer(totalSkipTime, 1000) {

            public void onTick(long millisUntilFinished) {
                currentRestTime = (int) (millisUntilFinished / 1000);
                pauseTimer = currentRestTime;
                mCircleBar.setProgress(currentRestTime);
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

                mCircleBar.setProgress(0);

                mActivity.StartPlayingFragment();
            }
        }.start();
    }

    private void addressTime() {
        mTimer.cancel();
        if (!pause) {
            currentRestTime *= 1000;
            currentRestTime += 5000;
            mCircleBar.setMax(currentRestTime / 1000);
            mCircleBar.setProgress(currentRestTime / 1000);
            startRestTimer(currentRestTime);
        } else {
            pauseTimer += 5;
            mCircleBar.setMax(pauseTimer);
            mCircleBar.setProgress(pauseTimer);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
    }

}
