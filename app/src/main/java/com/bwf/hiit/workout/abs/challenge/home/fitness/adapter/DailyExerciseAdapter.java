package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.DailyExerciseInfo;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.PlayingExercise;

import java.util.List;


public class DailyExerciseAdapter extends RecyclerView.Adapter<DailyExerciseAdapter.DailyExerciseDataHolder> {

    private DataModelWorkout dataModelWorkout;
    private DailyExerciseInfo info;
    private int day;
    private int plan;
    private boolean dataUp;
    private List<ExerciseDay> exerciseDays;

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase dataBase = AppDataBase.getInstance();

            exerciseDays = dataBase.exerciseDayDao().getExerciseDays(info.plan, info.day);
            for (ExerciseDay day : exerciseDays) {
                Exercise exercise = dataBase.exerciseDao().findById(day.getId());
                dataModelWorkout.dailyExercise_VideoView.add(exercise.getName());
                dataModelWorkout.exercisTimeList.add(day.getReps());
                dataModelWorkout.resetTimeList.add(day.getDelay());

                LogHelper.logD("1994:", "" + dataModelWorkout.dailyExercise_VideoView.size());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            LogHelper.logD("1994:", "" + dataModelWorkout.dailyExercise_VideoView.size());
            dataUp = true;
            notifyDataSetChanged();
        }
    }

    public DailyExerciseAdapter(DataModelWorkout dataModelWorkout, DailyExerciseInfo obj) {
        this.dataModelWorkout = dataModelWorkout;
        info = obj;
        dataUp = false;
        new GetDataFromDb().execute();
    }

    @NonNull
    @Override
    public DailyExerciseDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.exercise_info_item_activity, parent, false);
        return new DailyExerciseDataHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DailyExerciseDataHolder holder, final int position) {

        String nameOfExercise = dataModelWorkout.dailyExercise_ExerciseName.get(position);
        holder.nameOfExercise.setText(nameOfExercise);

        if (dataUp) {
            holder.exerciseTime.setText(dataModelWorkout.exercisTimeList.get(position) + "s");
            holder.restTime.setText(dataModelWorkout.resetTimeList.get(position) + "s");
            String videoPath = dataModelWorkout.dailyExercise_VideoView.get(position);
            int id = info.getApplication().getResources().getIdentifier(videoPath, "drawable", info.getApplication().getPackageName());
            //if(holder.imgeOfExercise!=null)
            Glide.with(info).load(id).into(holder.imgeOfExercise);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), PlayingExercise.class);
            i.putExtra(view.getContext().getString(R.string.day_selected), day);
            i.putExtra(view.getContext().getString(R.string.plan), plan);
            view.getContext().startActivity(i);
        });
    }

    @Override
    public void onViewRecycled(@NonNull DailyExerciseDataHolder holder) {
        holder.imgeOfExercise.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dataModelWorkout.dailyExercise_ExerciseName.size();
    }

    public void setDayPlan(int day,int plan) {
        this.day= day;
        this.plan= plan;
    }

    public void update(DataModelWorkout modelWorkout) {
        dataModelWorkout = modelWorkout;
        notifyDataSetChanged();
        dataUp = false;
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
