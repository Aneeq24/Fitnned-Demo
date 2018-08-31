package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.DailyExerciseInfo;
import com.dinuscxj.progressbar.CircleProgressBar;

public class DayRecycleAdapter extends RecyclerView.Adapter<DayRecycleAdapter.myHolder> {

    private String[] titles = {"BEGINNER", "INTERMEDIATE", "ADVANCED"};
    private DataModelWorkout dataModelWorkout;
    private int currentPlan;

    public DayRecycleAdapter(DataModelWorkout dataModelWorkout) {
        this.dataModelWorkout = dataModelWorkout;
        currentPlan = dataModelWorkout.curretPlan;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, final int position) {
        String nameOfApp = dataModelWorkout.dayName[position];
        float progress = 0;
        if (dataModelWorkout.progress.size() > position)
            progress = dataModelWorkout.progress.get(position);

        holder.tvDayName.setText(nameOfApp);
        holder.circleProgressBar.setProgress((int) (progress * 100));
        holder.itemView.setOnClickListener(view -> goToNewActivity(view.getContext(), position));
    }

    @Override
    public int getItemCount() {
        if (dataModelWorkout.dayName == null)
            return 0;
        else
            return dataModelWorkout.dayName.length;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvDayName;
        CircleProgressBar circleProgressBar;

        myHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.dayNameId);
            circleProgressBar = itemView.findViewById(R.id.line_progress_left);
        }
    }

    private void goToNewActivity(Context context, int position) {
        Intent i = new Intent(context, DailyExerciseInfo.class);
        i.putExtra(context.getString(R.string.day_selected), position + 1);
        i.putExtra(context.getString(R.string.plan), currentPlan);
        AnalyticsManager.getInstance().sendAnalytics("day  " + (position + 1) + "of_plan:" + titles[currentPlan - 1], "day_selected_" + (position + 1));
        context.startActivity(i);
    }

}
