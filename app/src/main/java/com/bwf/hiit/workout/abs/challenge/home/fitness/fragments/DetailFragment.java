package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.ContentAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Content;

import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DetailFragment extends Fragment {

    View rootView;
    TextView tvTypeName;
    TextView tvContent;
    ImageView imgHeader;
    RecyclerView rvFoodContent;
    List<Content> mList;
    Context context;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragement_detail, container, false);

        tvTypeName = rootView.findViewById(R.id.tv_type_name);
        imgHeader = rootView.findViewById(R.id.img_header);
        tvContent = rootView.findViewById(R.id.tv_content);
        rvFoodContent = rootView.findViewById(R.id.rv_food_content);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.source);
        requestOptions.error(R.drawable.source);

        context= getContext();


        if (getArguments() != null) {
            tvTypeName.setText(getArguments().getString("heading"));
            mList = getArguments().getParcelableArrayList("content");
            assert mList != null;
            tvContent.setText(mList.get(0).getText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            if (SharedPrefHelper.readBoolean(context, context.getString(R.string.is_load))) {
                String temp = context.getCacheDir().getAbsolutePath() + "/" + mList.get(0).getImage() + ".jpg";
                Glide.with(context).load(temp).into(imgHeader);
            } else {
                Glide.with(context).load(mList.get(0).getUrl()).into(imgHeader);
            }
            if (mList.size() > 1) {
                rvFoodContent.setVisibility(View.VISIBLE);
                ContentAdapter mAdapter = new ContentAdapter(context, mList.subList(1, mList.size()));
                rvFoodContent.setLayoutManager(new LinearLayoutManager(context));
                rvFoodContent.setAdapter(mAdapter);
            }
            AnalyticsManager.getInstance().sendAnalytics(getArguments().getString("heading"), getArguments().getString("heading"));
        }
        return rootView;
    }

}
