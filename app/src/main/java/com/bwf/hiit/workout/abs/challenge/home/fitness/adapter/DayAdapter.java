package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.myHolder> {

    private String[] days;
    private String[] date;

    public DayAdapter(String[] days) {
        this.days = days;
        date = new String[7];
        DateFormat format = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            date[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {
        holder.tvDay.setText(days[position]);
        holder.tvDate.setText(date[position]);
        if (days[position].equals(getCurrentDay())) {
            holder.itemView.setBackgroundResource(R.drawable.bg_item_day_round_fill);
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDate.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (days != null)
            return days.length;
        else return 0;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvDay;

        myHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDay = itemView.findViewById(R.id.tv_day);
        }
    }

    private String getCurrentDay() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEE");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
