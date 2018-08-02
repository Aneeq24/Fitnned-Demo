package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ScrollingActivity;

import java.util.ArrayList;
import java.util.List;


public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuItemHolder>
{
    String[] tilte;
    Bitmap[] images;
    String[] explanation;


    boolean dataUp = false;

    AppDataBase dataBase;

    List<ExerciseDay> exerciseDays;
    List<Integer> progress = new ArrayList<Integer>();
    List<Integer> maxValue;
    boolean isDataUp;
    int val=0;
    int max = 30;

    public  class  GetDataFromDb extends AsyncTask<Void , Void , Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            isDataUp = false;

            for (int plan = 1; plan < 4; plan++)
            {
                val = 0;
                for (int i = 0; i < 30; i++) {
                    int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getExerciseComplete();
                    int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getTotalExercise();


                    float v = (float) totalComplete / (float) totalExercises;

                 //   LogHelper.logD("1994:", "" + v);
                    if (v >= 1)
                    {
                        val++;
                    //    LogHelper.logD("1994:", "" + val);
                    }

                }

                LogHelper.logD("1994: val" , "" + val);
                progress.add(val);

        }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if(isCancelled())
                return;

            isDataUp = true;
            notifyDataSetChanged();

        }
    }

    public MainMenuAdapter(String[] tilte, Bitmap[] images , String[] explanation)
    {
        this.tilte = tilte;
        this.images = images;
        this.explanation = explanation;
        dataBase = AppDataBase.getInstance();
        GetDataFromDb getDataFromDb = new GetDataFromDb();
        getDataFromDb.execute();
    }

    @NonNull
    @Override
    public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(R.layout.menu_item_layout, parent , false);
        return new MainMenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position)
    {

       String t = tilte[position];
       String d = explanation[position];
       Bitmap img = images[position];
       holder.ti.setText(t);
    //   holder.e.setText(d);
       holder.pic.setImageBitmap(img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AppStateManager.mainCategory = position +1;
//
                Intent i = new Intent(view.getContext() ,ScrollingActivity.class);
                i.putExtra(view.getContext().getString(R.string.plan) ,(position+1));
                view.getContext().startActivity(i);
                AnalyticsManager.getInstance().sendAnalytics("plan_selected", "value " + (position+1));
            }
        });


        if (isDataUp)
        {
            holder.progressBar.setMax(30);
            holder.progressBar.setProgress(progress.get(position));
            int i = 30-progress.get(position);
            holder.dayLeftText.setText(i+"");
            holder.percentageText.setText(progress.get(position) + "%");

        }

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

             //   AppStateManager.mainCategory = position +1;

                Intent i = new Intent(view.getContext() ,ScrollingActivity.class);
                int tempValue = position+1;
                i.putExtra(view.getContext().getString(R.string.plan) ,tempValue);
                AnalyticsManager.getInstance().sendAnalytics("plan", "selected " + (position+1));
                view.getContext().startActivity(i);

            }
        });

    }

    public  void  updateRecycleView()
    {
        GetDataFromDb dataFromDb = new GetDataFromDb();
        dataFromDb.execute();

    }

    @Override
    public int getItemCount()
    {
        return tilte.length;
    }

    public  class  MainMenuItemHolder extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView ti;
        TextView e;
        private View v;
        private ImageView start;
        ProgressBar progressBar;
        TextView dayLeftText;
        TextView percentageText;



        public MainMenuItemHolder(View itemView)
        {
            super(itemView);
            pic = itemView.findViewById(R.id.taskImageId);
            ti = itemView.findViewById(R.id.tileId);
         //   e = itemView.findViewById(R.id.);
            start= itemView.findViewById(R.id.start);
            progressBar = itemView.findViewById(R.id.progressBar);
            dayLeftText = itemView.findViewById(R.id.home_textViewDaysleft);
            percentageText = itemView.findViewById(R.id.home_textVewPercentage);

        }

    }

}
