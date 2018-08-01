package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

import java.math.BigDecimal;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompleteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompleteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PlayingExercise playingExercise;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    Toolbar toolbar;

    ImageView backButton;

    TextView totalExercisTextView;
    TextView totalTimeSpendCount;



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
        rootView = inflater.inflate(R.layout.fragment_complete, container, false);


        toolbar = rootView.findViewById(R.id.toolbar10);

        totalExercisTextView = rootView.findViewById(R.id.cf_exerciseNo);
        totalTimeSpendCount = rootView.findViewById(R.id.cf_totalTime);

        PlayingExercise playingExercise = (PlayingExercise) getActivity();


//        int  minutes = (playingExercise.totaTimeSpend % 3600) / 60;
//        int seconds = playingExercise.totaTimeSpend % 60;
//
//        String timeString = String.format("%02d:%02d", minutes, seconds);

        totalExercisTextView.setText(playingExercise.totalExercises);
      //  totalTimeSpendCount.setText(timeString);


        toolbar.setNavigationOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CompleteFragment.this).commit();
                getActivity().finish();
            }
        });

        return rootView;
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
