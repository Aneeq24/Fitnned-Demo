package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NextFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    PlayingExercise playingExercise;
    ImageView timerUp;
    ImageView skip;
    ImageView aimationImage;
    TextView nextSreenExerciseName;
    int pauseTimer = 0;
    public boolean pause = false;

    TextView textView;

    CircleProgressBar mCustomCircleBar;

    ImageView pauseResumeImage;
    View rootView;


    private OnFragmentInteractionListener mListener;

    public NextFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NextFragment newInstance(String param1, String param2) {
        NextFragment fragment = new NextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_next, container, false);

        LinearLayout linearLayout = rootView.findViewById(R.id.fbNative);


        pauseResumeImage = rootView.findViewById(R.id.nf_pauseTimerImageView);
        mCustomCircleBar = rootView.findViewById(R.id.restTimer);

        textView = rootView.findViewById(R.id.cf_roundText);


        pause = false;

       mCustomCircleBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return progress + "s";
            }
        });



        //TODO native ad
        AdsManager.getInstance().showFacebookNativeAd(Application.getContext() , linearLayout , null);


        playingExercise = (PlayingExercise)getActivity();

        mCustomCircleBar.setMax(playingExercise.restTime);

        aimationImage = rootView.findViewById(R.id.nf_exerciseImage);

        if (playingExercise.currentRound<=(playingExercise.totalRounds-1))
            textView.setText("Round " + (playingExercise.currentRound +1)+"/" + playingExercise.totalRounds);
        else
            textView.setText("Round " + playingExercise.totalRounds+"/" + playingExercise.totalRounds);
        mCustomCircleBar.setProgress(playingExercise.restTime );

        mCustomCircleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseOrRenume();
            }
        });

        startRestTimer(playingExercise.restTime  *1000, 1000);
        

        initNext();

        return  rootView;
    }

    public void  pauseOrRenume()
    {
        pause = !pause;

        if (pause)
        {
            pauseResumeImage.setImageResource(R.drawable.play_screen_play_icon);
            countDownTimer.cancel();
        }
        else
         {
             if(pauseTimer<900)
             {
                 pauseTimer *= 1000;
             }
             startRestTimer(pauseTimer , 1000);
             pauseResumeImage.setImageResource(R.drawable.play_screen_pause_btn);

         }

    }


    void  initNext()
    {
          timerUp = rootView.findViewById(R.id.addRestTime); //addRestTime
          skip    =  rootView.findViewById(R.id.iv_timer);
          ImageView view = rootView.findViewById(R.id.iv_timer);
          nextSreenExerciseName = rootView.findViewById(R.id.tv_nextHeadline);
          view.setOnClickListener(view13 -> playingExercise.StartPlayingFragment());
          timerUp.setOnClickListener(view1 -> addrestTime());
          skip.setOnClickListener(view12 -> {

              playingExercise.StartPlayingFragment();
          });



          nextSreenExerciseName.setText(playingExercise.nextExerciseName);

        String str =  playingExercise.nextExerciseImage;

        int id = getResources().getIdentifier(str, "drawable",rootView.getContext().getPackageName());

        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

        //  viewVideo.setVideoPath(path);
        Glide.with(this).load(path).into(aimationImage);

      //  TTSManager.getInstance(playingExercise.getApplication()).play("The Next Exercise is " + playingExercise.displayName);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    CountDownTimer countDownTimer;
    int remaingTime;



    void  restCompleted()
    {

    }

    void  setTimerUpButton()
    {

    }

    void  setSkipButton()
    {

        playingExercise.skipRestNextButtonClicked();
    }

    int resetTime = 0;
    int currentRestTime= 0;


    void  startRestTimer(int totalSkipTime , int interval)
    {

        countDownTimer = new CountDownTimer(totalSkipTime, interval) {

            public void onTick(long millisUntilFinished)
            {
                currentRestTime = (int) (millisUntilFinished / 1000);
                pauseTimer = currentRestTime;
                mCustomCircleBar.setProgress(currentRestTime);
                int id = getResources().getIdentifier("clock", "raw",rootView.getContext().getPackageName());
                Utils.playAudio(rootView.getContext(),id);
            }

            public void onFinish()
            {
                int id = getResources().getIdentifier("ding", "raw",rootView.getContext().getPackageName());
                Utils.playAudio(rootView.getContext() , id);

                mCustomCircleBar.setProgress(0);
                playingExercise.StartPlayingFragment();
            }
        }.start();

    }


     void addrestTime()
    {
        countDownTimer.cancel();
        currentRestTime *=1000;
        currentRestTime += 5000;
         mCustomCircleBar.setMax(currentRestTime/1000);
        // mCustomCircleBar.setProgress(currentRestTime/1000);



        if(!pause)
        {
            pauseTimer = currentRestTime;
            startRestTimer(currentRestTime, 1000);
        }
        else
        {
            pauseTimer = currentRestTime/1000;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
