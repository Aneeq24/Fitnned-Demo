package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Content;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.myHolder> {

    private Context context;
    private List<Content> mList;

    public ContentAdapter(Context context, List<Content> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_content, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {
        holder.tvTitle.setText(mList.get(position).getText());

        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.is_load))) {
            String temp = context.getCacheDir().getAbsolutePath() + "/" + mList.get(position).getImage() + ".jpg";
            Glide.with(context).load(temp).into(holder.imgMain);
        } else {
            Glide.with(context).load(mList.get(position).getUrl()).into(holder.imgMain);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgMain;

        myHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tileId);
            imgMain = itemView.findViewById(R.id.img_main);
        }
    }

}
