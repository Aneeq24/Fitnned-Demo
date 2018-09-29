package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.DailyExerciseInfo;


public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.myHolder> {

    private String[] dayName;
    private String[] dayTime;
    private int currentPlan;

    public WorkoutAdapter(Context context, int plan) {
        this.currentPlan = plan;
        this.dayName = context.getResources().getStringArray(R.array.exercise_list);
        this.dayTime = context.getResources().getStringArray(R.array.exercise_time_list);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, final int position) {
        switch (position) {
            case 0:
                holder.img.setImageResource(R.drawable.workout_screen_pre_workout_warm_up);
                break;
            case 1:
                holder.img.setImageResource(R.drawable.workout_screen_post_workout_cool_down);
                break;
            case 2:
                holder.img.setImageResource(R.drawable.workout_screen_5_min_plank_challenge_image);
                break;
            case 3:
                holder.img.setImageResource(R.drawable.workout_screen_two_minute_abs_image);
                break;
        }
        holder.tvDayName.setText(dayName[position]);
        holder.txtTime.setText(dayTime[position]);
        holder.itemView.setOnClickListener(view -> goToNewActivity(view.getContext(), position));
    }

    @Override
    public int getItemCount() {
        if (dayName == null)
            return 0;
        else
            return dayName.length;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvDayName;
        TextView txtTime;
        ImageView img;

        myHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tv_exerciseName);
            txtTime = itemView.findViewById(R.id.tv_min);
            img = itemView.findViewById(R.id.img);
        }
    }

    private void goToNewActivity(Context context, int position) {
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.is_load))) {
            Intent i = new Intent(context, DailyExerciseInfo.class);
            i.putExtra(context.getString(R.string.day_selected), position + 1);
            i.putExtra(context.getString(R.string.plan), currentPlan);
            context.startActivity(i);
            SharedPrefHelper.writeBoolean(context, "is_first_run", true);
        } else Utils.showConnectionUsDialog(context);
    }

}
