package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

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


        AdsManager.getInstance().showFacebookInterstitialAd();

        AdsManager.getInstance().showFacebookNativeAd(Application.getContext() , linearLayout , null);


        playingExercise = (PlayingExercise)getActivity();

        initNext();

        return  rootView;
    }


    void  initNext()
    {
          timerUp = rootView.findViewById(R.id.addRestTime); //addRestTime
          skip    =  rootView.findViewById(R.id.nextScreenSkip);

          ImageView view = rootView.findViewById(R.id.nextExercisePlay);

          view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {


                  playingExercise.StartSkipFragment();

              }
          });

          timerUp.setOnClickListener(view1 -> setTimerUpButton());

          skip.setOnClickListener(view12 -> {

              setSkipButton();
              setSkipButton();
          });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    CountDownTimer countDownTimer;
    int remaingTime;


    void startPlayingExercise(int totalSkipTime, int interval, final TextView timer) {


        countDownTimer = new CountDownTimer(totalSkipTime, interval)
        {
            public void onTick(long millisUntilFinished)
            {
                remaingTime = (int)(millisUntilFinished/1000);
                timer.setText("" + millisUntilFinished / 1000 + "\"");
            }

            public void onFinish()
            {
               restCompleted();
            }
        }.start();

    }

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


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link CompleteFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class CompleteFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        private OnFragmentInteractionListener mListener;

        public CompleteFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompleteFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static CompleteFragment newInstance(String param1, String param2) {
            CompleteFragment fragment = new CompleteFragment();
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
            return inflater.inflate(R.layout.fragment_complete, container, false);
        }

        // TODO: Rename method, update argument and hook method into UI event
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }
    }
}
