package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;


public class DailyExerciseAdapter  extends RecyclerView.Adapter<DailyExerciseAdapter.DailyExerciseDataHolder>
{

    DataModelWorkout dataModelWorkout;


    View rootView;

    public  DailyExerciseAdapter( DataModelWorkout dataModelWorkout)
    {
        this.dataModelWorkout = dataModelWorkout;
    }

    @NonNull
    @Override
    public DailyExerciseDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(R.layout.exercise_info_item_activity, parent , false);
        return new DailyExerciseDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyExerciseDataHolder holder, final int position) {

        String nameOfExercise = dataModelWorkout.dailyExercise_ExerciseName.get(position);
        int imageID = dataModelWorkout.dailyExercise_ImageIndex.get(position);

        holder.nameOfExercise.setText(nameOfExercise);

        holder.imgeOfExercise.setImageResource(imageID);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return dataModelWorkout.dailyExercise_ExerciseName.size();
    }

    public  class  DailyExerciseDataHolder extends RecyclerView.ViewHolder
    {
        ImageView imgeOfExercise;
        TextView nameOfExercise;
        ImageView bgImage;
        TextView textColor;

        public DailyExerciseDataHolder(View itemView)
        {
            super(itemView);
            imgeOfExercise = itemView.findViewById(R.id.exerciseInfo_Icon);
            nameOfExercise = itemView.findViewById(R.id.exerciseInfo_ExerciseName);
            bgImage = itemView.findViewById(R.id.bgForDailyPerformance);
            textColor = itemView.findViewById(R.id.dayNameId);
        }
    }





}
