package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ScrollingActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MainMenuItemHolder> {

    private String[] tilte;
    private Bitmap[] images;
    private List<Integer> progress;
    private boolean isDataUp;


    public HomeAdapter(String[] tilte, Bitmap[] images) {
        this.tilte = tilte;
        this.images = images;
        progress = new ArrayList<>();
        new GetDataFromDb().execute();
    }

    @NonNull
    @Override
    public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent, false);
        return new MainMenuItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position) {

        holder.tvTitle.setText(tilte[position]);
        holder.imgBackground.setImageBitmap(images[position]);

        if (isDataUp) {
            holder.progressBar.setMax(30);
            holder.progressBar.setProgress(progress.get(position));
            int i = 30 - progress.get(position);
            holder.tvDayLeft.setText(i + "");
            holder.tvPercentage.setText(progress.get(position) + "%");
        }

        holder.imgBackground.setOnClickListener(view -> setOnClick(view,position));
        holder.imgStart.setOnClickListener(view -> setOnClick(view,position));

    }

    public void updateRecycleView() {
        new GetDataFromDb().execute();
    }

    @Override
    public int getItemCount() {
        return tilte.length;
    }

    class MainMenuItemHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgStart;
        ImageView imgBackground;
        ProgressBar progressBar;
        TextView tvDayLeft;
        TextView tvPercentage;

        MainMenuItemHolder(View itemView) {
            super(itemView);
            imgBackground = itemView.findViewById(R.id.taskImageId);
            tvTitle = itemView.findViewById(R.id.tileId);
            imgStart = itemView.findViewById(R.id.start);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvDayLeft = itemView.findViewById(R.id.home_textViewDaysleft);
            tvPercentage = itemView.findViewById(R.id.home_textVewPercentage);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase dataBase = AppDataBase.getInstance();
            isDataUp = false;
            for (int plan = 1; plan < 4; plan++) {
                int val = 0;
                for (int i = 0; i < 30; i++) {
                    int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getExerciseComplete();
                    int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan,
                            i + 1).get(0).getTotalExercise();

                    float v = (float) totalComplete / (float) totalExercises;

                    if (v >= 1) {
                        val++;
                    }
                }
                LogHelper.logD("1994: val", "" + val);
                progress.add(val);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;

            isDataUp = true;
            notifyDataSetChanged();
        }
    }

    private void setOnClick(View view,int position){
        Intent i = new Intent(view.getContext(), ScrollingActivity.class);
        i.putExtra(view.getContext().getString(R.string.plan), (position + 1));
        view.getContext().startActivity(i);
        AnalyticsManager.getInstance().sendAnalytics("plan_selected", "value " + (position + 1));
    }

}
