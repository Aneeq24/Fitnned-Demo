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

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_exercise, container, false);






        playingExercise = (PlayingExercise) getActivity();



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

    void  findRefrence()
    {
        exerciseTimer = rootView.findViewById(R.id.timerExerciseText);
        pauseButton = rootView.findViewById(R.id.pause);
        exerciseImage = rootView.findViewById(R.id.animationImage);
        viewVideo = rootView.findViewById(R.id.videoViewId);
       //Glide.with(this).load(R.drawable.girl).into(exerciseImage);
        helpButton = rootView.findViewById(R.id.help);


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

//        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + R.raw.video_file;
//        view.setVideoURI(Uri.parse(path));
//        view.start();


        String  path = "android.resource://" + rootView.getContext().getPackageName() + "/" + R.raw.girl_render;




        viewVideo.setVideoPath(path);

        viewVideo.start();


        viewVideo.setOnCompletionListener(mediaPlayer -> viewVideo.start());

        startVideo();

        if (!PlayingExercise.is_Paused) {
            AppDataBase dataBase = AppDataBase.getInstance();

            int value = playingExercise.getCurrentReps();


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


    void  startVideo()
    {
        countDownTimer = new CountDownTimer(1000, 1)
        {
            public void onTick(long millisUntilFinished) {

                remaingTime = (int)(millisUntilFinished/1000);
              //  timer.setText("" + millisUntilFinished / 1000 + "\"");


            }

            public void onFinish()
            {
               viewVideo.setAlpha(1);
            }
        }.start();

    }

    void startPlayingExercise(int totalSkipTime, int interval, final TextView timer) {


        countDownTimer = new CountDownTimer(totalSkipTime, interval)
        {
            public void onTick(long millisUntilFinished) {

                remaingTime = (int)(millisUntilFinished/1000);
                timer.setText("" + millisUntilFinished / 1000 + "\"");


            }

            public void onFinish()
            {
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

    void  pause()
    {
        countDownTimer.cancel();
        playingExercise.PauseFragment(remaingTime);
        remaingTime = 0;
    }

    void  helpButtonClick()
    {
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
