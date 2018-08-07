package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.HomeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Workout extends Fragment {

    String[] titles = {"BEGINEER", "INTERMEDIATE", "ADVANCED"};

    Bitmap[] image;

    @BindView(R.id.rv_main_container)
    RecyclerView rvMainContainer;
    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_workout, container, false);
        Log.d("1994 :", "oncreateview");
        initApp();
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initApp() {
        image = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_beginner_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_intermediate_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_advanced_image)
        };

        rvMainContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMainContainer.setAdapter(new HomeAdapter(titles, image));
    }

}
