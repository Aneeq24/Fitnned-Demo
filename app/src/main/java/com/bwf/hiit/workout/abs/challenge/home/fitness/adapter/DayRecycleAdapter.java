package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class DayRecycleAdapter extends RecyclerView.Adapter<DayRecycleAdapter.DayItemHolder> {

    private DataModelWorkout dataModelWorkout;
    private int currentPlan;
    private ScrollingActivity ac;

    @SuppressLint("StaticFieldLeak")
    private class GetDataFromDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase dataBase = AppDataBase.getInstance();

            for (int i = 0; i < 30; i++) {

                int totalComplete = dataBase.exerciseDayDao().getExerciseDays(currentPlan,
                        i + 1).get(0).getExerciseComplete();
                int totalExercises = dataBase.exerciseDayDao().getExerciseDays(currentPlan,
                        i + 1).get(0).getTotalExercise();

                float v = (float) totalComplete / (float) totalExercises;

                dataModelWorkout.progress.add(i, v);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isCancelled())
                return;
            notifyDataSetChanged();
        }

    }

    public DayRecycleAdapter(ScrollingActivity activity, DataModelWorkout dataModelWorkout) {
        this.dataModelWorkout = dataModelWorkout;
        ac = activity;
        currentPlan = dataModelWorkout.curretPlan;
        new GetDataFromDb().execute();
    }

    @NonNull
    @Override
    public DayItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_layout, parent, false);
        return new DayItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DayItemHolder holder, final int position) {
        String nameOfApp = dataModelWorkout.dayName[position];

        float progress = 0;
        if (dataModelWorkout.progress.size() > position) {
            progress = dataModelWorkout.progress.get(position);
        }

        holder.tvDayName.setText(nameOfApp);
        holder.circleProgressBar.setProgress((int) (progress * 100));

        holder.itemView.setOnClickListener(view -> {
            if (holder.circleProgressBar.getProgress() == 100)
                setResetCheckDialog(view.getContext(), position);
            else
                goToNewActivity(view.getContext(), position,false);
        });
    }

    @Override
    public int getItemCount() {
        return dataModelWorkout.dayName.length;
    }

    class DayItemHolder extends RecyclerView.ViewHolder {

        TextView tvDayName;
        CircleProgressBar circleProgressBar;

        DayItemHolder(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.dayNameId);
            circleProgressBar = itemView.findViewById(R.id.line_progress_left);

        }
    }

    public void resetAdapter(DataModelWorkout modelWorkout) {
        dataModelWorkout = modelWorkout;
        new GetDataFromDb().execute();
    }

    private void setResetCheckDialog(Context context, int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(context.getString(R.string.app_name));

        // set dialog message
        alertDialogBuilder
                .setMessage("This Exercise is Done.\nDo you want to start again?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, id) -> {
                    dialog.cancel();
                    goToNewActivity(context, pos,true);
                }).setNegativeButton("NO", (dialog, id) -> {
            // if this button is clicked, just close
            // the dialog box and do nothing
            dialog.cancel();
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void goToNewActivity(Context context, int position,boolean reset) {
        Intent i = new Intent(ac, DailyExerciseInfo.class);
        i.putExtra(context.getString(R.string.day_selected), position + 1);
        i.putExtra(context.getString(R.string.plan), currentPlan);
        i.putExtra("reset", reset);
        AnalyticsManager.getInstance().sendAnalytics("day_selected", "day  " + (position + 1) + "of_plan:" + currentPlan);
        context.startActivity(i);
    }

}
