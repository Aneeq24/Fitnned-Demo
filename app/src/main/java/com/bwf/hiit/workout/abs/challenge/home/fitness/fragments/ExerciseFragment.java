package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

public class ExerciseFragment extends Fragment {

    int value = 0;
    int timer = 0;
    int soundValue;
    int reamingTime;
    Context context;
    Handler handler;
    TextView tvTimer;
    ImageView btnHelp;
    TextView tvExName;
    ImageView btnNext;
    ImageView btnPause;
    ImageView btnSound;
    ImageView btnNoAds;
    TextView tvExercise;
    ImageView btnRateUs;
    CountDownTimer mTimer;
    ImageView imgExercise;
    ImageView btnPrevious;
    PlayingExercise mActivity;
    CountDownTimer mTotalTimer;
    CircleProgressBar mProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        context = getContext();
        handler = new Handler();
        mActivity = (PlayingExercise) getActivity();

        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        imgExercise = rootView.findViewById(R.id.animationImage);
        tvExercise = rootView.findViewById(R.id.ef_exerciseTextView);
        btnHelp = rootView.findViewById(R.id.btn_help);
        btnPause = rootView.findViewById(R.id.btn_pause);
        btnNext = rootView.findViewById(R.id.btn_next);
        btnPrevious = rootView.findViewById(R.id.btn_previous);
        tvExName = rootView.findViewById(R.id.tv_exerciseName_Playing);
        btnSound = rootView.findViewById(R.id.btn_sound);
        mProgressBar = rootView.findViewById(R.id.prog_timer);
        btnNoAds = rootView.findViewById(R.id.fab_no_ads);
        btnRateUs = rootView.findViewById(R.id.fab_rate_us);
        tvTimer = rootView.findViewById(R.id.tv_timer);

        mProgressBar.setProgressFormatter((progress, max) -> progress + "\"");
        mProgressBar.setOnClickListener(view -> pause());
        btnSound.setOnClickListener(view -> soundButton());
        btnPause.setOnClickListener(view -> pause());
        btnHelp.setOnClickListener(view -> helpButtonClick());
        btnNoAds.setOnClickListener(view -> mActivity.mBilling.purchaseRemoveAds());
        btnRateUs.setOnClickListener(view -> Utils.onRateUs(context));
        btnPrevious.setOnClickListener(view -> onExerciseComplete(false));
        btnNext.setOnClickListener(view -> onExerciseComplete(true));

        findRefrence();
        startTimer(mActivity.timer);
        return rootView;
    }

    private void findRefrence() {
        if (soundValue > 0)
            btnSound.setImageResource(R.drawable.play_screen_sound_on_btn);
        else
            btnSound.setImageResource(R.drawable.play_screen_sound_off_btn);

        soundValue = SharedPrefHelper.readInteger(context, "sound");

        assignTopUi();
        tvExName.setText(mActivity.displayName);

        int id = getResources().getIdentifier(mActivity.exerciseImage, "drawable", mActivity.getPackageName());

        if (id != 0) {
            String path = "android.resource://" + mActivity.getPackageName() + "/" + id;
            Glide.with(this).load(path).into(imgExercise);
        } else if (SharedPrefHelper.readBoolean(mActivity, getString(R.string.is_load))) {
            String temp = mActivity.getCacheDir().getAbsolutePath() + "/" + mActivity.exerciseImage + ".gif";
            Glide.with(this).load(temp).into(imgExercise);
        } else {
            Glide.with(this).load(mActivity.exerciseUrl).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(imgExercise);
        }

        if (!PlayingExercise.isPaused) {
            value = mActivity.getCurrentReps();
            TTSManager.getInstance(mActivity.getApplication()).play("Do " + mActivity.displayName + " for " + value / 1000 + " seconds");
            handler.postDelayed(() -> TTSManager.getInstance(mActivity.getApplication()).play(mActivity.ttsList.get(0).getText()),5000);
            startPlayingExercise(value);
        } else {
            PlayingExercise.isPaused = false;
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
        tvExercise.setText((mActivity.currentEx + 1) + " of " + mActivity.totalExercises);
    }

    private void startPlayingExercise(int totalSkipTime) {
        mProgressBar.setMax(value / 1000);
        mTimer = new CountDownTimer(totalSkipTime, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                reamingTime = (int) (millisUntilFinished / 1000);

                if (reamingTime > 3) {
                    int id = context.getResources().getIdentifier("clock", "raw", context.getPackageName());
                    Utils.playAudio(getContext(), id);
                } else {
                    TTSManager.getInstance(mActivity.getApplication()).play("" + reamingTime);
                }
                mProgressBar.setProgress(reamingTime);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                int id = context.getResources().getIdentifier("ding", "raw", context.getPackageName());
                Utils.playAudio(getContext(), id);
                onExerciseComplete(true);
            }
        }.start();
    }

    private void startTimer(int totalSkipTime) {
        totalSkipTime *= 1000;
        mTotalTimer = new CountDownTimer(totalSkipTime, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                timer = (int) (millisUntilFinished / 1000);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                @SuppressLint("DefaultLocale") String timeString = String.format("%02d:%02d", minutes, seconds);
                tvTimer.setText(timeString);
            }

            public void onFinish() {

            }
        }.start();
    }

    private void onExerciseComplete(boolean isNext) {
        handler.removeCallbacksAndMessages(null);
        int time;
        if (isNext)
            time = timer - reamingTime;
        else time = timer + 30;
        mActivity.NextFragment(isNext, time);
    }

    public void pause() {
        mTimer.cancel();
        mActivity.PauseFragment(reamingTime);
        reamingTime = 0;
    }

    private void helpButtonClick() {
        mTimer.cancel();
        mActivity.helpFragmentFun(reamingTime);
        reamingTime = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTotalTimer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTotalTimer.cancel();
    }

}
