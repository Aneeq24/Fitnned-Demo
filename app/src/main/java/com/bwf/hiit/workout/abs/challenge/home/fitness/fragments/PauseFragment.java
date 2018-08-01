package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PauseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PauseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PauseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView nextExerciseName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    View rootView;


    public PauseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PauseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PauseFragment newInstance(String param1, String param2) {
        PauseFragment fragment = new PauseFragment();
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


    ImageView resumeImg;
    ImageView prevImg;
    ImageView next;
    ImageView animationImage;

    PlayingExercise playingExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pause, container, false);

        LinearLayout fbNative = rootView.findViewById(R.id.fbNative);

        //TODO
        AdsManager.getInstance().showFacebookInterstitialAd();

        AdsManager.getInstance().showFacebookNativeAd(Application.getContext(),fbNative ,null);
        findReferences();
        return rootView;

    }

    void  findReferences()
    {

        playingExercise = (PlayingExercise) getActivity();
        resumeImg = rootView.findViewById(R.id.pauseResume);
        prevImg = rootView.findViewById(R.id.previous);
        next = rootView.findViewById(R.id.pauseNext);

        nextExerciseName = rootView.findViewById(R.id.tv_pauseHeadline);

        nextExerciseName.setText(playingExercise.nextExerciseName);
        animationImage = rootView.findViewById(R.id.pf_exerciseImage);

        String str =  playingExercise.nextExerciseImage;

        int id = getResources().getIdentifier(str, "drawable",rootView.getContext().getPackageName());

        String path = "android.resource://" + rootView.getContext().getPackageName() + "/" + id;

        //  viewVideo.setVideoPath(path);
        Glide.with(this).load(path).into(animationImage);

        resumeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResumeExercise();
            }
        });

        prevImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrevious();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNext();
            }
        });


        //TTSManager.getInstance(getApplication()).play The Next Exercise is [exercise name])

//        TTSManager.getInstance(playingExercise.getApplication()).play("The Next Exercise is " + playingExercise.nextExerciseName);

    }

    void  onResumeExercise()
    {
        playingExercise.onResumeFragment();
    }

    void  onNext()
    {

    }

    void  onPrevious()
    {

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
