package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AppPrefManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;
import com.dinuscxj.progressbar.CircleProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    VideoView viewVideo;
    PlayingExercise playingExercise;
    CircleProgressBar playingExerciseCircle;
    TextView  exerciseName;

    TextView roundTextView;
    TextView exerciseTextView;

    int soundValue;



    private OnFragmentInteractionListener mListener;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseFragment newInstance(String param1, String param2) {
        ExerciseFragment fragment = new ExerciseFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_exercise, container, false);

        playingExercise = (PlayingExercise) getActivity();

        playingExerciseCircle = rootView.findViewById(R.id.playingExericseCircle);

        playingExerciseCircle.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {

                return progress + "\"";
            }
        });

        playingExerciseCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                pause();
            }
        });

        findRefrence();
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
       countDownTimer.cancel();
    }

    TextView exerciseTimer;
    Button pauseButton;
    ImageView exerciseImage;
    ImageView helpButton;
    CountDownTimer countDownTimer;
    CountDownTimer videoTimer;

    ImageView soundButton_B;

    int value =0;
    void  findRefrence()
    {
        exerciseTimer = rootView.findViewById(R.id.timerExerciseText);
        pauseButton = rootView.findViewById(R.id.pause);
        exerciseImage = rootView.findViewById(R.id.animationImage);
        viewVideo = rootView.findViewById(R.id.videoViewId);
        exerciseTextView = rootView.findViewById(R.id.ef_exerciseTextView);
        roundTextView = rootView.findViewById(R.id.ef_roundTextView);
       //Glide.with(this).load(R.drawable.girl).into(exerciseImage);
        helpButton = rootView.findViewById(R.id.help);
        exerciseName = rootView.findViewById(R.id.tv_exerciseName_Playing);
        soundButton_B = rootView.findViewById(R.id.ef_soundFragment);

        com.google.android.gms.ads.AdView adView = rootView.findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

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

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                pause();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helpButtonClick();
            }
        });

        assignTopUi();

//        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + R.raw.video_file;
//        view.setVideoURI(Uri.parse(path));
//        view.start();


        String str =  playingExercise.exerciseName;

        exerciseName.setText(playingExercise.displayName);

         int id = getResources().getIdentifier(str, "drawable",rootView.getContext().getPackageName());

        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

      //  viewVideo.setVideoPath(path);
        Glide.with(this).load(path).into(exerciseImage);

       // viewVideo.start();


       // viewVideo.setOnCompletionListener(mediaPlayer -> viewVideo.start());

      //  startVideo();

        if (!PlayingExercise.is_Paused) {
            AppDataBase dataBase = AppDataBase.getInstance();

            value = playingExercise.getCurrentReps();

            TTSManager.getInstance(playingExercise.getApplication()).play("Do "+ playingExercise.displayName +" for " +value/1000+ " seconds");


            startPlayingExercise(value, 1000, exerciseTimer);
        }

        else
            {
                PlayingExercise.is_Paused = false;
                int val = PlayingExercise.pauseTimer;
                PlayingExercise.pauseTimer = 0;
                val = (val) *1000;
                startPlayingExercise(val , 1000, exerciseTimer);

            }
    }

    public int remaingTime;




    void  assignTopUi()
    {
        roundTextView.setText((playingExercise.currentRound+1) + "/"+ playingExercise.totalRounds);
        exerciseTextView.setText((playingExercise.currentExercise+1) + "/" + playingExercise.totalExercisePerRound);
    }

    void  startVideo()
    {
        videoTimer = new CountDownTimer(1000, 1)
        {
            public void onTick(long millisUntilFinished) {

              //  timer.setText("" + millisUntilFinished / 1000 + "\"");

            }

            public void onFinish()
            {
              // viewVideo.setAlpha(1);
            }
        }.start();

    }

    void startPlayingExercise(int totalSkipTime, int interval, final TextView timer) {

//      playingExerciseCircle.setMax(totalSkipTime/1000);

        playingExerciseCircle.setMax(value/1000);
        countDownTimer = new CountDownTimer(totalSkipTime, interval)
        {
            public void onTick(long millisUntilFinished) {

                remaingTime = (int)(millisUntilFinished/1000);
                timer.setText("" + millisUntilFinished / 1000 + "\"");

                int id = getResources().getIdentifier("clock", "raw",rootView.getContext().getPackageName());

                if(remaingTime>3)
                {
                    Utils.playAudio(rootView.getContext(),id);


                }
                else
                {
                    TTSManager.getInstance(playingExercise.getApplication()).play( "" +remaingTime);
                }

                playingExerciseCircle.setProgress(remaingTime);
            }

            public void onFinish()
            {
                int id = getResources().getIdentifier("ding", "raw",rootView.getContext().getPackageName());
                Utils.playAudio(rootView.getContext(),id);

              //  viewVideo.setAlpha(0);
                timer.setText("" + 0 + "\"");
                onExerciseComplete();
            }
        }.start();

    }
    void  resetCountDownTimer()
    {

    }

    void  onExerciseComplete()
    {
        playingExercise.NextFragment();
    }

   public   void  pause()
    {
       // viewVideo.setAlpha(0);
       countDownTimer.cancel();
        playingExercise.PauseFragment(remaingTime);
        remaingTime = 0;
    }

    void  helpButtonClick()
    {
    //    viewVideo.setAlpha(0);
        countDownTimer.cancel();
        playingExercise.helpFragmentFun(remaingTime);
        remaingTime =0;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      //  viewVideo.setAlpha(0);
       // videoTimer.cancel();
        countDownTimer.cancel();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
