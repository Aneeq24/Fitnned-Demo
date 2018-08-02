package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DataModelWorkout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.DailyExerciseInfo;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ScrollingActivity;
import com.dinuscxj.progressbar.CircleProgressBar;

public class DayRecycleAdapter extends RecyclerView.Adapter<DayRecycleAdapter.DayItemHolder>
{
    DataModelWorkout dataModelWorkout;

    SharedPreferences sharedPreferences;
    AppDataBase dataBase;

    boolean getValuefromDb;

    View rootView;

    public  int currentPlan = 0;



    public  class  GetDataFromDb extends AsyncTask<Void , Void , Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            dataBase = AppDataBase.getInstance();

            for(int i =0 ; i< 30 ; i++)
            {

                int totalComplete = dataBase.exerciseDayDao().getExerciseDays(currentPlan,
                        i+1).get(0).getExerciseComplete();
                int totalExercises = dataBase.exerciseDayDao().getExerciseDays(currentPlan,
                        i+1).get(0).getTotalExercise();


               float v = (float) totalComplete/(float) totalExercises;

               dataModelWorkout.progress.add(i,v);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isCancelled())
                return;
            notifyDataSetChanged();
        }
    }

    ScrollingActivity ac;
    GetDataFromDb data;

    public DayRecycleAdapter(ScrollingActivity activity , DataModelWorkout dataModelWorkout)
    {
        this.dataModelWorkout = dataModelWorkout;

        ac = activity;
        currentPlan = dataModelWorkout.curretPlan;


        dataBase = AppDataBase.getInstance();
        data = new GetDataFromDb();
        data.execute();

    }



    @NonNull
    @Override
    public DayItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(R.layout.day_item_layout, parent , false);
        rootView = view;

        return new DayItemHolder(view);
    }




   
    @Override
    public void onBindViewHolder(@NonNull final DayItemHolder holder, final int position) {
        String nameOfApp = dataModelWorkout.dayName[position];

        float progress = 0;
        if (dataModelWorkout.progress.size()>position) {
           progress  = dataModelWorkout.progress.get(position);
        }



        holder.dayName.setText(nameOfApp);
        holder.circleProgressBar.setProgress((int)(progress *100));





        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(ac , DailyExerciseInfo.class);
            i.putExtra(view.getContext().getString(R.string.day_selected) , position+1);
            i.putExtra(view.getContext().getString(R.string.plan) , currentPlan);
            AnalyticsManager.getInstance().sendAnalytics("day_selected", "day  " + (position+1) + "of_plan:" + currentPlan);
            view.getContext().startActivity(i);
        });


    }




   public void  resetAdapter(DataModelWorkout modelWorkout)
    {
        dataModelWorkout  = modelWorkout;
        data = new GetDataFromDb();
        data.execute();

    }



    @Override
    public int getItemCount()
    {
        return dataModelWorkout.dayName.length;
    }

    public  class  DayItemHolder extends RecyclerView.ViewHolder {

        TextView dayName;
        TextView progress;
        CircleProgressBar circleProgressBar;



        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public DayItemHolder(View itemView)
        {
            super(itemView);
          //  pic = itemView.findViewById(R.id.dayIconId);
            dayName = itemView.findViewById(R.id.dayNameId);
            circleProgressBar = itemView.findViewById(R.id.line_progress_left);
           // circleProgressBar.setBackground(rootView.getResources().getDrawable( R.drawable.play_screen_timer_loading_bg));

         //   circleProgressBar.



        }

    }
}
