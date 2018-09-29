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
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.DailyExerciseInfo;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.FoodDetailActivity;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.myHolder> {

    private String[] foodName;

    public FoodAdapter(Context context) {
        this.foodName = context.getResources().getStringArray(R.array.food_list);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, final int position) {
        switch (position) {
            case 0:
                holder.itemView.setBackgroundResource(R.drawable.ic_orange_round_bar);
                holder.img.setImageResource(R.drawable.food_screen_weight_loss_image);
                break;
            case 1:
                holder.itemView.setBackgroundResource(R.drawable.ic_red_round_bar);
                holder.img.setImageResource(R.drawable.food_screen_pre_workout_image);
                break;
            case 2:
                holder.itemView.setBackgroundResource(R.drawable.ic_blue_round_bar);
                holder.img.setImageResource(R.drawable.food_screen_post_workout_image);
                break;
            case 3:
                holder.itemView.setBackgroundResource(R.drawable.ic_green_round_bar);
                holder.img.setImageResource(R.drawable.food_screen_fat_burning_foods_image);
                break;
        }
        holder.tvDayName.setText(foodName[position]);
        holder.itemView.setOnClickListener(view -> goToNewActivity(view.getContext(), position));
    }

    @Override
    public int getItemCount() {
        if (foodName == null)
            return 0;
        else
            return foodName.length;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvDayName;
        ImageView img;

        myHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tv_exerciseName);
            img = itemView.findViewById(R.id.img);
        }
    }

    private void goToNewActivity(Context context, int pos) {
        Intent i = new Intent(context, FoodDetailActivity.class).putExtra("pos", pos);
        AnalyticsManager.getInstance().sendAnalytics("FoodDetailActivity", "FoodDetailActivity");
        context.startActivity(i);
    }

}
