package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

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

import java.util.List;


public class DailyExerciseAdapter  extends RecyclerView.Adapter<DailyExerciseAdapter.DailyExerciseDataHolder>
{

    DataModelWorkout dataModelWorkout;

    DailyExerciseInfo info;

    View rootView;

    boolean dataUp = false;

    AppDataBase dataBase;

    List<ExerciseDay> exerciseDays;

    public  class  GetDataFromDb extends AsyncTask<Void , Void , Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            dataBase = AppDataBase.getInstance();

            exerciseDays = dataBase.exerciseDayDao().getExerciseDays(info.plan, info.day);
            for (ExerciseDay day : exerciseDays)
            {
                Exercise exercise = dataBase.exerciseDao().findById(day.getId());
                dataModelWorkout.dailyExercise_VideoView.add(exercise.getName());
                LogHelper.logD("1994:","" + dataModelWorkout.dailyExercise_VideoView.size());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LogHelper.logD("1994:","" +dataModelWorkout.dailyExercise_VideoView.size());
            dataUp = true;
            notifyDataSetChanged();
        }
    }


    public  DailyExerciseAdapter( DataModelWorkout dataModelWorkout , DailyExerciseInfo obj)
    {
        this.dataModelWorkout = dataModelWorkout;
        info = obj;
        //GetDataFromDb db = new GetDataFromDb();
       // db.execute();
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


        holder.nameOfExercise.setText(nameOfExercise);

        Glide.with(info).load(R.drawable.animation).into(holder.imgeOfExercise);

        if(dataUp) {
            String videoPath = dataModelWorkout.dailyExercise_VideoView.get(position);

            int id = info.getApplication().getResources().getIdentifier(videoPath, "raw", info.getApplication().getPackageName());



            String path = "android.resource://" + info.getApplication().getPackageName() + "/" + id;

            holder.viewVideo.setVideoPath(path);
            LogHelper.logD("1994:", "" + videoPath);
            holder.viewVideo.start();
            holder.viewVideo.setOnCompletionListener(mediaPlayer ->
                    holder.viewVideo.start());

            holder.itemView.setOnClickListener(view -> {

            });
        }

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
        VideoView viewVideo;

        public DailyExerciseDataHolder(View itemView)
        {
            super(itemView);
            imgeOfExercise = itemView.findViewById(R.id.exerciseInfo_Icon);
            nameOfExercise = itemView.findViewById(R.id.exerciseInfo_ExerciseName);
            bgImage = itemView.findViewById(R.id.bgForDailyPerformance);
            textColor = itemView.findViewById(R.id.dayNameId);
            viewVideo = itemView.findViewById(R.id.vdExerciseVideo);
        }
    }





}
