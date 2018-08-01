package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AppPrefManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkipFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SkipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkipFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    boolean pause = false;
    ImageView pauseResumeImage;
    int soundValue;

    private OnFragmentInteractionListener mListener;

    public SkipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SkipFragment newInstance(String param1, String param2) {
        SkipFragment fragment = new SkipFragment();
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
        rootView = inflater.inflate(R.layout.fragment_skip, container, false);

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
            startSkipTimer(pauseTimer , 1000 , skipTimerText);
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

        AppPrefManager.getInstance().setValue("sound",soundValue);

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


        startSkipTimer(16000 , 1000,skipTimerText);

        skipCircleprogressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter()
        {
            @Override
            public CharSequence format(int progress, int max) {

                return progress + "\"";
            }
        });

        skipCircleprogressBar.setMax(15);

        skipCircleprogressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseOrRenume();
            }
        });


        soundButton_B = rootView.findViewById(R.id.sf_soundFragment);

        soundValue = AppPrefManager.getInstance().getValue("sound",0);

        if(soundValue>0)
        {
            soundButton_B.setImageResource(R.drawable.play_screen_sound_on_btn);
        }
        else
        {
            soundButton_B.setImageResource(R.drawable.play_screen_sound_off_btn);
        }

        soundButton_B.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                soundButton();
            }
        });



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

    void startSkipTimer(int totalSkipTime, int interval, final TextView timer) {

        countDownTimer = new CountDownTimer(totalSkipTime, interval) {

            public void onTick(long millisUntilFinished)
            {
                timer.setText("" + millisUntilFinished / 1000 + "\"");
                int value = (int)(millisUntilFinished / 1000);
                pauseTimer = value;
                skipCircleprogressBar.setProgress(value);
                int id = getResources().getIdentifier("clock", "raw",rootView.getContext().getPackageName());

                Utils.playAudio(rootView.getContext(),id);
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
        getActivity().getSupportFragmentManager().beginTransaction().remove(SkipFragment.this).commit();
        playingExercise.StartPlayingFragment();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
