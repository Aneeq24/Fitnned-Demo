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
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;

import java.util.List;


public class DailyExerciseAdapter extends RecyclerView.Adapter<DailyExerciseAdapter.DailyExerciseDataHolder> {

    private List<Exercise> exerciseList;
    private Activity info;
    private int day;
    private int plan;
    private int completeRounds;
    private int completeExercise;

    public DailyExerciseAdapter(Activity obj) {
        info = obj;
    }

    public void setList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DailyExerciseDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_info_item_activity, parent, false);
        return new DailyExerciseDataHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DailyExerciseDataHolder holder, final int position) {


        holder.nameOfExercise.setText(exerciseList.get(position).getDisplay_name());
        holder.exerciseTime.setText(exerciseList.get(position).getUnit() + "s");
        holder.restTime.setText(exerciseList.get(position).getLang() + "s");
        String videoPath = exerciseList.get(position).getName();
        int id = info.getApplication().getResources().getIdentifier(videoPath, "drawable", info.getApplication().getPackageName());

        Glide.with(info).load(id).into(holder.imgeOfExercise);

        holder.itemView.setOnClickListener(view -> {
            if (completeRounds > 0 || completeExercise > 0)
                Utils.setCheckBox(info, day, plan);
            else Utils.setScreen(info, day, plan);
        });
    }

    @Override
    public void onViewRecycled(@NonNull DailyExerciseDataHolder holder) {
        holder.imgeOfExercise.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null)
            return exerciseList.size();
        else return 0;
    }

    public void setDayPlan(int day, int plan) {
        this.day = day;
        this.plan = plan;
    }

    public void setData(int completeRounds, int completeExercise) {
        this.completeRounds = completeRounds;
        this.completeExercise = completeExercise;
    }

    class DailyExerciseDataHolder extends RecyclerView.ViewHolder {
        ImageView imgeOfExercise;
        TextView nameOfExercise;
        ImageView bgImage;
        TextView textColor;
        VideoView viewVideo;
        TextView exerciseTime;
        TextView restTime;

        DailyExerciseDataHolder(View itemView) {
            super(itemView);
            imgeOfExercise = itemView.findViewById(R.id.exerciseInfo_Icon);
            nameOfExercise = itemView.findViewById(R.id.exerciseInfo_ExerciseName);
            bgImage = itemView.findViewById(R.id.bgForDailyPerformance);
            textColor = itemView.findViewById(R.id.dayNameId);
            viewVideo = itemView.findViewById(R.id.vdExerciseVideo);
            exerciseTime = itemView.findViewById(R.id.edi_exerciseTime);
            restTime = itemView.findViewById(R.id.edi_exerciseRestTime);

        }
    }

}
