package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;

public class DetailFragment extends Fragment {

    View rootView;
    TextView tvTypeName;
    TextView tvHeading;
    TextView tvContent;
    ImageView imgHeader;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragement_detail, container, false);

        tvTypeName = rootView.findViewById(R.id.tv_type_name);
        imgHeader = rootView.findViewById(R.id.img_header);
        tvHeading = rootView.findViewById(R.id.tv_heading);
        tvContent = rootView.findViewById(R.id.tv_content);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.source);
        requestOptions.error(R.drawable.source);

        if (getArguments() != null) {
            tvTypeName.setText(getArguments().getString("name"));
            tvHeading.setText(getArguments().getString("heading"));
            tvContent.setText(getArguments().getString("content"));
            Glide.with(this).load(getArguments().getString("image")).apply(requestOptions).into(imgHeader);
            AnalyticsManager.getInstance().sendAnalytics(getArguments().getString("name"), getArguments().getString("name"));
        }
        return rootView;
    }

}
