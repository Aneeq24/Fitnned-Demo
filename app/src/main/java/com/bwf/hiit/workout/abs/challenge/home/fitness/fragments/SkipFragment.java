package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

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

    View rootView;

    TextView headingNameExercise;

    PlayingExercise playingExercise;

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
        findReferences();
        return  rootView;

    }

    TextView skipTimerText;
    TextView skipTimerButton;
    ImageView skipImg;
    CountDownTimer countDownTimer;

    void  findReferences()
    {
        playingExercise = (PlayingExercise)getActivity();
        skipTimerText = rootView.findViewById(R.id.timerSkipText);
        skipTimerButton = rootView.findViewById(R.id.skipButton);
        skipImg = rootView.findViewById(R.id.skipLayoutImag);

        headingNameExercise = rootView.findViewById(R.id.tv_exercise_name_skipScreen);

        headingNameExercise.setText(playingExercise.displayName);

        skipTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayingButton();


            }
        });

        startSkipTimer(15000 , 1000,skipTimerText);

    }

    void startSkipTimer(int totalSkipTime, int interval, final TextView timer) {

        countDownTimer = new CountDownTimer(totalSkipTime, interval) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000 + "\"");
            }

            public void onFinish()
            {
                startPlayingButton();
            }
        }.start();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    void  startPlayingButton()
    {
        countDownTimer.cancel();

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
