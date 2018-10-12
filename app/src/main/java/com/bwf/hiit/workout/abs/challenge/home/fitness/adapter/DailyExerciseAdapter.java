package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;

import java.util.List;


public class DailyExerciseAdapter extends RecyclerView.Adapter<DailyExerciseAdapter.myHolder> {

    private List<Exercise> exerciseList;
    private Activity info;

    public DailyExerciseAdapter(Activity obj) {
        info = obj;
    }

    public void setList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_info_item_activity, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {

        holder.tvExerciseName.setText(exerciseList.get(position).getDisplay());
        holder.tvMin.setText(exerciseList.get(position).getUnit() + "s");

        int id = info.getResources().getIdentifier(exerciseList.get(position).getName(), "drawable", info.getPackageName());
        if (id != 0) {
            String path = "android.resource://" + info.getPackageName() + "/" + id;
            Glide.with(info).load(path).thumbnail(Glide.with(info).load(R.drawable.load)).into(holder.imgExercise);
        } else {
            Glide.with(info).load(exerciseList.get(position).getUrl()).thumbnail(Glide.with(info).load(R.drawable.load)).into(holder.imgExercise);
        }
//        holder.itemView.setOnClickListener(v -> Utils.showTipDialog(info, exerciseList.get(position).getDisplay(), exerciseList.get(position).getName(),exerciseList.get(position).getUrl()));
    }

    @Override
    public void onViewRecycled(@NonNull myHolder holder) {
        holder.imgExercise.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null)
            return exerciseList.size();
        else return 0;
    }

    class myHolder extends RecyclerView.ViewHolder {
        ImageView imgExercise;
        TextView tvExerciseName;
        TextView tvMin;

        myHolder(View itemView) {
            super(itemView);
            imgExercise = itemView.findViewById(R.id.exerciseInfo_Icon);
            tvExerciseName = itemView.findViewById(R.id.tv_exerciseName);
            tvMin = itemView.findViewById(R.id.tv_min);
        }
    }

}
